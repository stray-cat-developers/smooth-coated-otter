package io.mustelidae.otter.sumatrana.api.config

import com.asarkar.spring.test.redis.AutoConfigureEmbeddedRedis
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc

@ActiveProfiles("embedded")
@ExtendWith(SpringExtension::class)
@ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator::class)
@SpringBootTest(classes = [io.mustelidae.otter.sumatrana.SumatranaOtterApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [DefaultEmbeddedMongo::class, DefaultEmbeddedRedis::class])
@AutoConfigureMockMvc
@AutoConfigureEmbeddedRedis(port = 0)
class FlowTestSupport {

    @Autowired
    final lateinit var mockMvc: MockMvc
}
