package com.laxmiclinic.config;

import com.laxmiclinic.model.AdminUser;
import com.laxmiclinic.repository.AdminUserRepository;
import com.laxmiclinic.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClinicInfoService clinicInfoService;
    private final ShiftService shiftService;
    private final NotificationService notificationService;

    @Value("${app.admin.default-username:Rajesh Sonar}")
    private String defaultAdminUsername;

    @Value("${app.admin.default-password:rajeshsonarlc}")
    private String defaultAdminPassword;

    @Value("${app.admin.default-email:rajeshsonar@laxmiclinic.local}")
    private String defaultAdminEmail;

    public DataInitializer(AdminUserRepository adminUserRepository,
                           PasswordEncoder passwordEncoder,
                           ClinicInfoService clinicInfoService,
                           ShiftService shiftService,
                           NotificationService notificationService) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.clinicInfoService = clinicInfoService;
        this.shiftService = shiftService;
        this.notificationService = notificationService;
    }

    @Override
    public void run(String... args) {
        logger.info("Initializing Laxmi Clinic System Data...");

        // 1. Initialize Admin User (Dr. Rajesh Sonar)
        adminUserRepository.deleteAll();
        AdminUser admin = new AdminUser(
                defaultAdminUsername,
                passwordEncoder.encode(defaultAdminPassword),
                "Dr. Rajesh Sonar",
                defaultAdminEmail,
                "ROLE_ADMIN"
        );
        adminUserRepository.save(admin);
        logger.info("Initialized administrator account: '{}'.", defaultAdminUsername);

        // 2. Initialize / Refresh Clinic Info & Doctor Details (Dr. Rajesh Sonar)
        clinicInfoService.initDefaultClinicInfo();
        logger.info("Initialized Doctor Profile & Clinic Info for Dr. Rajesh Sonar (B.A.M.S. BOM, ESIS Panel Doctor).");

        // 3. Initialize / Refresh 2 Shifts for Dr. Rajesh Sonar's consultation timings
        shiftService.initDefault5Shifts();
        logger.info("Initialized 2 Consultation Shifts matching Doctor Timings (12:00 PM - 05:00 PM & 07:00 PM - 12:00 AM).");

        // 4. Log completion
        logger.info("Laxmi Clinic Management System initialized successfully.");
    }
}
