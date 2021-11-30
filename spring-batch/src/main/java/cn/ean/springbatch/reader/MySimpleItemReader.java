package cn.ean.springbatch.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;


import java.util.Iterator;
import java.util.List;

/**
 * @author ean
 * @FileName MySimpleItemReader
 * @Date 2021/11/30 4:40 下午
 **/
public class MySimpleItemReader implements ItemReader<String> {

    private Iterator<String> iterator;

    public MySimpleItemReader(List<String> data) {
        this.iterator = data.iterator();
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        // 数据一个接着一个读取
        return iterator.hasNext() ? iterator.next() : null;
    }
}
