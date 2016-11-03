package de.unidue.ltl.hci.millionaire.util;

import info.bliki.api.Page;
import info.bliki.api.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants;
import de.tudarmstadt.ukp.wikipedia.api.sweble.PlainTextConverter;

public class WikipediaAccess
{

    private User user;
    private SimpleWikiConfiguration config;
    private Compiler compiler;

    public WikipediaAccess(String wikiapiUrl)
            throws Exception
    {
        user = new User("", "", wikiapiUrl);
        user.login();
        
        config = new SimpleWikiConfiguration(WikiConstants.SWEBLE_CONFIG);
        
        compiler = new Compiler(config);
    }

    public List<String> getWikiContent(String title)
        throws Exception
    {
    	File file = new File("target/wiki_cache/" + title + ".txt");
    	if (file.exists()) {
    		return FileUtils.readLines(file);
    	}
    	
    	StringBuilder sb = new StringBuilder();
        List<String> contents = new ArrayList<String>();
        for (Page page : user.queryContent(new String[] { title })) {
        	String plainText = getPlainText(page);
            contents.add(plainText);
            sb.append(plainText);
            sb.append("\n");
        }

        
        FileUtils.writeStringToFile(file, sb.toString());

        return contents;
    }

    private String getPlainText(Page page)
        throws Exception
    {
        return (String) parsePage(page, new PlainTextConverter());
    }

    private Object parsePage(Page page, AstVisitor v)
        throws Exception
    {
        // Use the provided visitor to parse the page
        return v.go(getCompiledPage(page).getPage());
    }

    private CompiledPage getCompiledPage(Page page)
        throws Exception
    {
        CompiledPage cp;
        try {
            PageTitle pageTitle = PageTitle.make(config, page.getTitle());
            PageId pageId = new PageId(pageTitle, -1);


            cp = compiler.postprocess(pageId, page.getCurrentContent(), null);
        }
        catch (Exception e) {
            throw new Exception(e);
        }
        return cp;
    }

    public static void main(String[] args)
        throws Exception
    {

        User user = new User("", "", "http://en.wikipedia.org/w/api.php");
        user.login();

        String[] listOfTitleStrings = { "Automobile" };
        List<Page> listOfPages = user.queryContent(listOfTitleStrings);
        for (Page page : listOfPages) {
            System.out.println(page.getTitle());
            System.out.println(page.getCurrentContent());
        }
    }
}
