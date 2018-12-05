package ru.BlackAndWhite.CuteJavaPet.serviceIntegrationTests;

import lombok.extern.log4j.Log4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.BlackAndWhite.CuteJavaPet.common.CreateThings;
import ru.BlackAndWhite.CuteJavaPet.common.ServiceTestConfig;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.GroupDAO;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.services.AttachmentService;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Log4j
@PropertySource(value = {"classpath:/uploadStatuses.properties"})

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {ServiceTestConfig.class})
//
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ServiceTestConfig.class})
public class AttachmentIntegrationTest {
    private static boolean isInitialized = false;

    @Resource
    Environment environment;

    @Autowired
    ApplicationContext context;

    @Qualifier("dataSource")
    @Autowired
    javax.sql.DataSource dataSource;

    @Inject
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private GroupDAO GroupDAO;

    @Before
    public void runOnce() {
        if (isInitialized) return;

        try {
            JdbcTestUtils.dropTables(jdbcTemplate,
                    "fileformats",
                    "users_groups",
                    "users_roles",
                    "attachments_groups",
                    "attachments",
                    "`groups`",
                    "roles",
                    "users");
        } catch (Exception e) {
            log.info("can`t delete tables");
        }
        log.info("create tables in test schema");
        ResourceDatabasePopulator tables = new ResourceDatabasePopulator();
        tables.addScript(new ClassPathResource("/sqlScripts/createTables.sql"));
        DatabasePopulatorUtils.execute(tables, dataSource);
        isInitialized = true;
        tables = new ResourceDatabasePopulator();
        log.info("insert test data to tables");
        tables.addScript(new ClassPathResource("/sqlScripts/insertData.sql"));
        DatabasePopulatorUtils.execute(tables, dataSource);
    }

    @Test
    public void saveAttach() throws Exception {
        //empty files
        //wrong format
        //was run "saveAttachAndNotice"
        //was run "generateAndSendNotice"
        log.info("saveAttach");
        List<Group> gList = new ArrayList<>();
        gList.add(CreateThings.newGroup(1));
        Mockito.when(GroupDAO.getEmailsByGroups(gList)).thenReturn(Arrays.asList("one email", "second"));
        AttachmentService attachmentService =  context.getBean(AttachmentService.class);
        List<String> statuses = attachmentService.saveAttachments("default description",
                CreateThings.newMultipartFileArray());
        Assert.assertEquals(statuses.get(0), environment.getProperty("empty"));
        Assert.assertEquals(statuses.get(1), environment.getProperty("wrongFormat"));
        Assert.assertEquals(statuses.get(2), environment.getProperty("success"));
    }

//    @Test
//    public void selectAttachmentByID() throws Exception {
//        log.info("selectAttachByID() ");
//        Attach selectAttachId = attachmentService.selectAttachmentByID(1);
//        Assert.assertSame(selectAttachId.getId(), 1);
//    }
//
//    @Test
//    public void addAttachmentGroup() throws Exception {
//        log.info("addAttachmentGroup()");
//        final Integer GROUP_ID = 1;
//        final Integer ATTACH_ID = 1;
////        attachmentService.addAttachmentGroups(CreateThings.newAttach(ATTACH_ID),
////                CreateThings.newGroup(GROUP_ID));
////        List<Attach> attachList = attachmentService.selectAttachesByGroupID(GROUP_ID);
////        Assert.assertEquals(1, attachList.size());
////        Assert.assertEquals(attachList.get(0).getId(), ATTACH_ID);
//    }
}