package com.blogspot.jabelarminecraft.examplemod.client.localization;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.concurrent.ConcurrentSkipListSet;

@SideOnly(Side.CLIENT)
public class ModLocale extends Locale
{
    /** Splits on "=" */
    private static final Splitter SPLITTER = Splitter.on('=').limit(2);
    private static final Pattern PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    public static Map<String, String> properties = Maps.<String, String>newHashMap();
    private boolean unicode;

    /**
     * For each domain $D and language $L, attempts to load the resource $D:lang/$L.lang
     */
    @Override
    public synchronized void loadLocaleDataFiles(IResourceManager resourceManager, List<String> languageList)
    {
        // DEBUG
        System.out.println("loadLocaleDataFiles() for language list = "+languageList);
        
        this.properties.clear();

        for (String s : languageList)
        {
            String s1 = String.format("lang/%s.lang", s);
            
            // DEBUG
            System.out.println("Loading lang file = "+s1);
            System.out.println("Will loop through following resource domains: "+resourceManager.getResourceDomains());

            SortedSet<String> sortedResourceDomains = new ConcurrentSkipListSet<String>(new DomainComparator());
            for (String domainString : resourceManager.getResourceDomains())
            {
                sortedResourceDomains.add(domainString);
            }

            // DEBUG
            System.out.println("Sorted resource domains = "+sortedResourceDomains);

            for (String s2 : sortedResourceDomains)
            {
                // DEBUG
                System.out.println("For resource domain = "+s2);

                try
                {
                    this.loadLocaleData(resourceManager.getAllResources(new ResourceLocation(s2, s1)));
                }
                catch (IOException var9)
                {
                    ;
                }
            }
        }
        
        // DEBUG
        System.out.println("After loading, lang file value for stone = "+this.properties.get("tile.stone.stone.name"));

        this.checkUnicode();
    }

    @Override
    public boolean isUnicode()
    {
        return this.unicode;
    }

    private void checkUnicode()
    {
        this.unicode = false;
        int i = 0;
        int j = 0;

        for (String s : this.properties.values())
        {
            int k = s.length();
            j += k;

            for (int l = 0; l < k; ++l)
            {
                if (s.charAt(l) >= 256)
                {
                    ++i;
                }
            }
        }

        float f = (float)i / (float)j;
        this.unicode = f > 0.1D;
    }

    /**
     * Loads the locale data for the list of resources.
     */
    private void loadLocaleData(List<IResource> resourcesList) throws IOException
    {
        for (IResource iresource : resourcesList)
        {
            // DEBUG
            System.out.println("For resource = "+iresource.getResourcePackName());
            
            InputStream inputstream = iresource.getInputStream();

            try
            {
                this.loadLocaleData(inputstream);
            }
            finally
            {
                IOUtils.closeQuietly(inputstream);
            }
        }
    }

    private void loadLocaleData(InputStream inputStreamIn) throws IOException
    {
        inputStreamIn = net.minecraftforge.fml.common.FMLCommonHandler.instance().loadLanguage(properties, inputStreamIn);
        if (inputStreamIn == null) return;
        for (String s : IOUtils.readLines(inputStreamIn, StandardCharsets.UTF_8))
        {
            if (!s.isEmpty() && s.charAt(0) != '#')
            {
                String[] astring = Iterables.toArray(SPLITTER.split(s), String.class);

                if (astring != null && astring.length == 2)
                {
                    String s1 = astring[0];
                    String s2 = PATTERN.matcher(astring[1]).replaceAll("%$1s");
                    this.properties.put(s1, s2);
                    
                    // DEBUG
                    if (s1 == "tile.stone.stone.name")
                    {
                        System.out.println("Putting lang key value pair for stone = "+s2);
                    }
                }
            }
        }
    }

    /**
     * Returns the translation, or the key itself if the key could not be translated.
     */
    private String translateKeyPrivate(String translateKey)
    {
        String s = this.properties.get(translateKey);
        return s == null ? translateKey : s;
    }

    /**
     * Calls String.format(translateKey(key), params)
     */
    @Override
    public String formatMessage(String translateKey, Object[] parameters)
    {
        String s = this.translateKeyPrivate(translateKey);

        try
        {
            return String.format(s, parameters);
        }
        catch (IllegalFormatException var5)
        {
            return "Format error: " + s;
        }
    }

    @Override
    public boolean hasKey(String key)
    {
        return this.properties.containsKey(key);
    }

    class DomainComparator implements Comparator<String>
    {
        public int compare(String domain1, String domain2)
        {
            // DEBUG
            System.out.println("Comparing "+domain1+" to "+domain2);

            if (domain1.equals("realms"))
            {
                return -1;
            }
            else
            {
                if (domain2.equals("realms"))
                {
                    return 1;
                }
            }

            if (domain1.equals(".mcassetsroot"))
            {
                return -1;
            }
            else
            {
                if (domain2.equals(".mcassetsroot"))
                {
                    return 1;
                }
            }

            if (domain1.equals("fml"))
            {
                return -1;
            }
            else
            {
                if (domain2.equals("fml"))
                {
                    return 1;
                }
            }

            if (domain1.equals("forge"))
            {
                return -1;
            }
            else
            {
                if (domain2.equals("forge"))
                {
                    return 1;
                }
            }

            if (domain1.equals("minecraft"))
            {
                return 1;
            }
            else
            {
                if (domain2.equals("minecraft"))
            {
                    return -1;
                }
            }

            return 0;
        }
    }
}
