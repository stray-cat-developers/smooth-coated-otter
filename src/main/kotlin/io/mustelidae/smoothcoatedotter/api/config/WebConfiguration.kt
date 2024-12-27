package io.mustelidae.smoothcoatedotter.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.mustelidae.smoothcoatedotter.api.lock.UserLockInterceptor
import io.mustelidae.smoothcoatedotter.api.scope.BlockCertainProfileInterceptor
import io.mustelidae.smoothcoatedotter.utils.Jackson
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.format.support.FormattingConversionService
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import java.time.format.DateTimeFormatter

@Configuration
@ControllerAdvice
class WebConfiguration(
    private val userLockInterceptor: UserLockInterceptor,
    private val blockCertainProfileInterceptor: BlockCertainProfileInterceptor,
) : DelegatingWebMvcConfiguration() {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(userLockInterceptor)
        registry.addInterceptor(blockCertainProfileInterceptor)
        super.addInterceptors(registry)
    }

    @Bean
    fun requestResponseLogFilter(): FilterRegistrationBean<RequestResponseLogFilter> =
        FilterRegistrationBean<RequestResponseLogFilter>().apply {
            filter = RequestResponseLogFilter()
        }

    @Bean
    override fun mvcConversionService(): FormattingConversionService {
        val conversionService = super.mvcConversionService()
        val dateTimeRegistrar = DateTimeFormatterRegistrar()
        dateTimeRegistrar.setDateFormatter(DateTimeFormatter.ISO_DATE)
        dateTimeRegistrar.setTimeFormatter(DateTimeFormatter.ISO_TIME)
        dateTimeRegistrar.setDateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME)
        dateTimeRegistrar.registerFormatters(conversionService)
        return conversionService
    }

    @Bean
    fun mappingJackson2HttpMessageConverter(objectMapper: ObjectMapper?): MappingJackson2HttpMessageConverter =
        MappingJackson2HttpMessageConverter(Jackson.getMapper())
}
