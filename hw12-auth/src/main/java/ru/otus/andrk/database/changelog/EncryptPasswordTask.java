package ru.otus.andrk.database.changelog;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log4j2
public class EncryptPasswordTask implements CustomTaskChange {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private boolean executed = false;


    @Override
    public void execute(Database database) throws CustomChangeException {
        if (executed) {
            return; //TODO: обхожу описанное в https://github.com/liquibase/liquibase/issues/3945
        }
        try {
            log.debug("Encrypt initial passwords");
            JdbcConnection conn = (JdbcConnection) database.getConnection();
            var selectStatement = conn.prepareStatement("select id, password from users");
            var updateStatement = conn.prepareStatement("update users set password=? where id=?");
            var rs = selectStatement.executeQuery();
            while (rs.next()) {
                var id = rs.getLong("id");
                var oldPassword = rs.getString("password");
                var newPassword = encoder.encode(oldPassword);
                updateStatement.setLong(2, id);
                updateStatement.setString(1, newPassword);
                updateStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new CustomChangeException(e);
        }
        executed = true;
    }

    @Override
    public String getConfirmationMessage() {
        return null;
    }

    @Override
    public void setUp() throws SetupException {

    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }
}
