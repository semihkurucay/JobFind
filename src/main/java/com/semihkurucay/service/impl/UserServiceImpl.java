package com.semihkurucay.service.impl;

import com.semihkurucay.dto.*;
import com.semihkurucay.entity.*;
import com.semihkurucay.exception.BaseException;
import com.semihkurucay.exception.ErrorMessage;
import com.semihkurucay.exception.ErrorType;
import com.semihkurucay.mapper.AddressMapper;
import com.semihkurucay.mapper.CvMapper;
import com.semihkurucay.mapper.UserMapper;
import com.semihkurucay.repository.*;
import com.semihkurucay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CvRepository cvRepository;
    private final CvSchoolRepository cvSchoolRepository;
    private final CvExperienceRepository  cvExperienceRepository;
    private final CvLanguageRepository cvLanguageRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;
    private final CvMapper cvMapper;
    private final AddressMapper addressMapper;

    private User getUser(String username){
        return userRepository.findByLogin_Username(username)
                .orElseThrow(() -> new BaseException(new ErrorMessage(username, ErrorType.NO_RECORD_EXIT)));
    }

    @Override
    public DtoUser findByLogin_Username(String username) {
        return userMapper.toDtoUser(getUser(username));
    }

    @Transactional
    @Override
    public DtoUser update(String username, DtoUserIU dtoUserIU) {

        User user = getUser(username);

        if(user.getAddress() == null){
            user.setAddress(new Address());
        }

        user.setAddress(addressMapper.toUpdated(dtoUserIU.getAddress(), user.getAddress()));
        user = userMapper.toUpdated(dtoUserIU, user);

        if(dtoUserIU.getPassword() != null && !dtoUserIU.getPassword().isEmpty()){
            user.getLogin().setPassword(bCryptPasswordEncoder.encode(dtoUserIU.getPassword()));
        }

        return userMapper.toDtoUser(userRepository.save(user));
    }

    @Override
    public DtoCv findByUser_Login_Username(String username) {
        Cv cv = cvRepository.findByUser_Login_Username(username)
                .orElseThrow(() -> new BaseException(new ErrorMessage(username, ErrorType.NO_RECORD_EXIT)));

        cv.setUser(null);

        return cvMapper.toDto(cv);
    }

    @Override
    public DtoCv findByUser_Id(Long id) {
        Cv cv = cvRepository.findByUser_Id(id)
                .orElseThrow(() -> new BaseException(new ErrorMessage(id.toString(), ErrorType.NO_RECORD_EXIT)));

        return cvMapper.toDto(cv);
    }

    private Cv getCv(String username){
        Cv cv = cvRepository.findByUser_Login_Username(username)
                .orElseGet(() -> new Cv());

        if(cv.getUser() == null){
            cv.setUser(getUser(username));
        }

        return cv;
    }

    @Transactional
    @Override
    public DtoCv saveCv(String username, DtoCvIU dtoCvIU) {
        return cvMapper.toDto(
                cvRepository.save(
                        cvMapper.toUpdated(dtoCvIU, getCv(username))));
    }

    private Cv getCvCompanet(String username){
        Cv cv = getCv(username);
        return cv.getId() == null ? cvRepository.save(cv) : cv;
    }

    @Transactional
    @Override
    public DtoCvSchool saveCvSchool(String username, DtoCvSchoolIU dtoCvSchoolIU) {
        if(dtoCvSchoolIU.getEndDate() != null && dtoCvSchoolIU.getEndDate().isBefore(dtoCvSchoolIU.getStartDate())){
            throw new BaseException(new ErrorMessage(dtoCvSchoolIU.getEndDate().toString(), ErrorType.ERROR_DATE));
        }

        CvSchool cvSchool = cvMapper.toEntity(dtoCvSchoolIU);
        cvSchool.setCv(getCvCompanet(username));

        return cvMapper.toDto(cvSchoolRepository.save(cvSchool));
    }

    @Transactional
    @Override
    public void deleteCvSchool(String username, Long id) {
        cvSchoolRepository.deleteById_AndCv_User_Login_Username(username, id);
    }

    @Transactional
    @Override
    public DtoCvLanguage saveCvLanguage(String username, DtoCvLanguageIU dtoCvLanguageIU) {
        CvLanguage cvLanguage = cvMapper.toEntity(dtoCvLanguageIU);
        cvLanguage.setCv(getCvCompanet(username));

        return cvMapper.toDto(cvLanguageRepository.save(cvLanguage));
    }

    @Transactional
    @Override
    public void deleteCvLanguage(String username, Long id) {
        cvLanguageRepository.deleteById_AndCv_User_Login_Username(username, id);
    }

    @Transactional
    @Override
    public DtoCvExperience saveCvExperience(String username, DtoCvExperienceIU dtoCvExperienceIU) {
        if(dtoCvExperienceIU.getEndDate() != null && dtoCvExperienceIU.getEndDate().isBefore(dtoCvExperienceIU.getStartDate())){
            throw new BaseException(new ErrorMessage(dtoCvExperienceIU.getEndDate().toString(), ErrorType.ERROR_DATE));
        }

        CvExperience cvExperience = cvMapper.toEntity(dtoCvExperienceIU);
        cvExperience.setCv(getCvCompanet(username));

        return cvMapper.toDto(cvExperienceRepository.save(cvExperience));
    }

    @Transactional
    @Override
    public void deleteCvExperience(String username, Long id) {
        cvExperienceRepository.deleteById_AndCv_User_Login_Username(username, id);
    }
}
