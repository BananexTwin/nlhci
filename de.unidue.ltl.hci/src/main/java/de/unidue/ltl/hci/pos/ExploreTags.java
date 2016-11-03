package de.unidue.ltl.hci.pos;

import dkpro.toolbox.core.TaggedToken;
import dkpro.toolbox.core.util.FD;
import dkpro.toolbox.corpus.BrownCorpus;
import dkpro.toolbox.corpus.analyzed.AnalyzedCorpus;

public class ExploreTags {

    public static void main(String[] args)
        throws Exception
    {       
        AnalyzedCorpus brown = new AnalyzedCorpus(new BrownCorpus());
        FD<String> fd = new FD<String>();

        for (TaggedToken taggedToken : brown.getTaggedTokens()) {
            fd.inc(taggedToken.getTag().getOriginalTag());
        }
        System.out.println(fd.getN());
        System.out.println(fd.getB());
        System.out.println(fd.getCount("NIL"));
    }
}
