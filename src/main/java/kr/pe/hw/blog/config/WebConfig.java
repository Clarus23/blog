package kr.pe.hw.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.dir}" + "summernote-images/") // 외부 저장소 경로를 properties에서 가져옴
    private String fileDirectory;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/summernote-images/**")
                .addResourceLocations("file:///" + fileDirectory); // 외부경로를 url 로 매핑
    }
}
