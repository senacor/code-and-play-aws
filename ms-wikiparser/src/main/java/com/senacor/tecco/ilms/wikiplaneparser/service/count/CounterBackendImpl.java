package com.senacor.tecco.ilms.wikiplaneparser.service.count;

import com.senacor.tecco.ilms.common.utils.DelayUtil;
import com.senacor.tecco.ilms.common.utils.ReactiveUtil;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.StringTokenizer;

/**
 * @author Andreas Keefer
 */
@Component
public class CounterBackendImpl implements CounterBackend {

    @Autowired
    private DelayUtil delayUtil;

    @Override
    public int countWords(final ParsedPage parsedPage) {
        delayUtil.delay(100);
        Validate.notNull(parsedPage, "parsedPage must not be null");
        String text = parsedPage.getText();
        int wordCount = new StringTokenizer(text, " ").countTokens();
        ReactiveUtil.print("countWords: %s", wordCount);
        return wordCount;
    }
}
