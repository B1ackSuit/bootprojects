package cn.ean.springbatch.job;


import cn.ean.springbatch.pojo.TestData;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

// @Component
public class FileItemReaderDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private ItemReader<TestData> fileItemReader() {
        FlatFileItemReader<TestData> reader = new FlatFileItemReader<>();

        reader.setResource(new ClassPathResource("file"));
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        tokenizer.setNames("id", "field1", "field2", "field3");

        DefaultLineMapper<TestData> mapper = new DefaultLineMapper<>();

        mapper.setLineTokenizer(tokenizer);

        mapper.setFieldSetMapper(fieldSet -> {
            TestData testData = new TestData();
            testData.setId(fieldSet.readInt("id"));
            testData.setField1(fieldSet.readString("field1"));
            testData.setField2(fieldSet.readString("field2"));
            testData.setField3(fieldSet.readString("field3"));

            return testData;
        });

        reader.setLineMapper(mapper);
        return reader;
    }

    private Step step() {
        return stepBuilderFactory.get("step")
                .<TestData, TestData>chunk(2)
                .reader(fileItemReader())
                .writer(list -> list.forEach(System.out::println))
                .build();
    }


    @Bean
    public Job fileItemReaderJob() {
        return jobBuilderFactory.get("fileItemReaderJob")
                .start(step())
                .build();
    }
}
