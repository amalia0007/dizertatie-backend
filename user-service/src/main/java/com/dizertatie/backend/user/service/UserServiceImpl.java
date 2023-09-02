package com.dizertatie.backend.user.service;

import com.dizertatie.backend.user.exception.EmailExistsException;
import com.dizertatie.backend.user.exception.PaginationSortingException;
import com.dizertatie.backend.user.model.*;
import com.dizertatie.backend.user.pojo.ResponsePageList;
import com.dizertatie.backend.user.repository.CompanyRepository;
import com.dizertatie.backend.user.repository.RoleRepository;
import com.dizertatie.backend.user.repository.UserRepository;
import com.dizertatie.backend.user.util.SecurityUtil;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final PasswordEncoder bcryptEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserGameService userGameService;
    private final CompanyRepository companyRepository;

    public UserServiceImpl(PasswordEncoder bcryptEncoder, RoleRepository roleRepository, UserRepository userRepository, UserGameService userGameService, CompanyRepository companyRepository) {
        this.bcryptEncoder = bcryptEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userGameService = userGameService;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {

        User databaseUser = userRepository.findByEmail(user.getEmail());

        databaseUser.setSkipped(user.isSkipped());
        databaseUser.getCategories().clear();
        databaseUser.getCategories().addAll(user.getCategories());
        userRepository.save(databaseUser);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User registerNewUserAccount(RegistrationDTO userDto) throws EmailExistsException {
        if (emailExists(userDto.getEmail())) {
            throw new EmailExistsException(
                    "There is an existent account with the email address: "
                            + userDto.getEmail());
        }

        User newUser = new User();


        Role userRole = roleRepository.findByType("ROLE_USER");
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(bcryptEncoder.encode(userDto.getPassword()));
        newUser.setRoles(Collections.singleton(userRole));
        newUser.setAdmin(false);

        if (companyRepository.findByName(userDto.getCompany()) != null) {
            Company company = companyRepository.findByName(userDto.getCompany());
            newUser.setCompany(company);
        } else {
            Company company = new Company();
            company.setName(userDto.getCompany());
            newUser.setCompany(company);
            companyRepository.save(company);
        }


        return userRepository.save(newUser);
    }


    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(final String email) {
        User foundUser = userRepository.findByEmail(email);
        System.out.println(foundUser);
        return (userRepository.findByEmail(email) != null);
    }


    public void saveNewPassword(String email, String password) {
        User foundUser = userRepository.findByEmail(email);
        foundUser.setPassword(bcryptEncoder.encode(password));
        userRepository.save(foundUser);
    }

    @Override
    public ResponsePageList<User> findPaginatedUsers(String orderBy, String direction, int page, int size, String query) {

        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = Sort.by(Sort.Direction.ASC, orderBy);
        }
        if (direction.equals("DESC")) {
            sort = Sort.by(Sort.Direction.DESC, orderBy);
        }

        if (!(direction.equals(Direction.ASCENDING.getDirectionCode()) || direction.equals(Direction.DESCENDING.getDirectionCode()))) {
            throw new PaginationSortingException("Invalid sort direction");
        }
        if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.TITLE.getOrderByCode()))) {
            throw new PaginationSortingException("Invalid orderBy condition");
        }

        Predicate<User> firstNameExist = user -> user.getFirstName().toLowerCase().contains(query.toLowerCase());
        Predicate<User> lastNameExist = user -> user.getLastName().toLowerCase().contains(query.toLowerCase());
        assert sort != null;
        List<User> list = userRepository.findAll(sort).stream().filter(firstNameExist.or(lastNameExist)).collect(Collectors.toList());

        PagedListHolder<User> pagedListHolder = new PagedListHolder<>(list);
        pagedListHolder.setPageSize(size);
        pagedListHolder.setPage(page);
        ResponsePageList<User> response = new ResponsePageList<>();
        response.setNrOfElements(pagedListHolder.getNrOfElements());
        response.setPageList(pagedListHolder.getPageList());
        return response;

    }

    @Override
    public ResponsePageList<User> findPaginatedUsers(String userName, Pageable pageable) {
        if (SecurityUtil.hasRole("ROLE_ADMIN")) {
            Long companyId = SecurityUtil.currentUser().getCompany().getCompany_id();
            Page<User> usersByCompany = userRepository.findAllByCompanyAndUserName(companyId, userName, pageable);
            return new ResponsePageList<User>(usersByCompany.getNumberOfElements(), usersByCompany.getContent());
        } else {
            Page<User> users = userRepository.findAllByUserName(userName, pageable);
            return new ResponsePageList<User>(users.getNumberOfElements(), users.getContent());
        }
    }

    @Override
    public void clearPenalties(User u) {
        List<Penalty> listForRemove = new ArrayList<>();
        userRepository.findById(u.getId()).ifPresent(user -> user.getPenalties().forEach(penalty -> {
            if (penalty.getPenaltyAddedDate().isBefore(LocalDate.now())) {
                listForRemove.add(penalty);
            }
        }));
        User user = userRepository.findById(u.getId()).orElse(null);
        assert user != null;
        if (user.getPenalties().size() > 0)
            user.getPenalties().removeAll(listForRemove);
        System.out.println(user.getPenalties().size());
        userRepository.saveAndFlush(user);
    }

    @Override
    public void addOnePenalty(User u) {
        User user = userRepository.findById(u.getId()).orElse(null);
        assert user != null;
        user.addPenalty(new Penalty(LocalDate.now().plusMonths(Penalty.oneMonth)));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void removeOnePenalty(User u, String penaltyId) {
        User user = userRepository.findById(u.getId()).orElse(null);
        Penalty penalty = null;
        assert user != null;
        for (Penalty p : user.getPenalties()) {
            if (p.getId() == Long.parseLong(penaltyId)) {
                penalty = p;
            }
        }
        user.getPenalties().remove(penalty);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void checkForPenalties(User user) {
        userGameService.getUserGames().stream().filter(t -> t.getUser().getId().equals(user.getId())).forEach(penalty -> {
            if (!penalty.isGeneratedPenalty() & penalty.getReturn_date().isBefore(LocalDate.now())) {
                penalty.getUser().addPenalty(new Penalty(LocalDate.now().plusMonths(Penalty.numberOfMonthsPenaltyExist)));
                userGameService.changeUserGamePenalty(penalty.getId());
            }
        });
    }

    @Override
    public void saveUserImg(User user) {
        User databaseUser = userRepository.findByEmail(user.getEmail());
        databaseUser.setFirstName(user.getFirstName());
        databaseUser.setLastName(user.getLastName());
        databaseUser.setImg(user.getImg());
        userRepository.save(databaseUser);
    }

    @Override
    public void updateUserBanned(User user) {
        User databaseUser = userRepository.findByEmail(user.getEmail());
        databaseUser.setBanned(user.isBanned());
        userRepository.save(databaseUser);
    }


    @Override
    public void makeAdmin(User user) {
        User dbUser = userRepository.findByEmail(user.getEmail());
        Role userRole = roleRepository.findByType("ROLE_ADMIN");
        dbUser.setAdmin(true);
        dbUser.setRoles(new HashSet<>(Collections.singleton(userRole)));
        userRepository.save(dbUser);
    }

    @Override
    public void makeAdmin(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user found for the given id!"));

        if (user.currentlyAdmin()) {
            throw new RuntimeException("The user is already an admin!");
        }

        Role userRole = roleRepository.findByType("ROLE_ADMIN");
        user.getRoles().add(userRole);
        user.setAdmin(true);
        userRepository.save(user);
    }

    @Override
    public void updateUserBanUntil(User user) {
        User databaseUser = userRepository.findByEmail(user.getEmail());
        databaseUser.setBanned(user.isBanned());
        databaseUser.setBanUntil(user.getBanUntil());
        userRepository.save(databaseUser);
    }
}
