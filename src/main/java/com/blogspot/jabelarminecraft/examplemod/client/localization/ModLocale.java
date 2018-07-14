/**
    Copyright (C) 2017 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/
package com.blogspot.jabelarminecraft.examplemod.client.localization;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
@SideOnly(Side.CLIENT)
public class ModLocale extends Locale
{
    /** Splits on "=" */
    private static final Splitter SPLITTER = Splitter.on('=').limit(2);
    private static final Pattern PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    public Map<String, String> properties = Maps.<String, String>newHashMap();
    private boolean unicode;

    /**
     * For each domain $D and language $L, attempts to load the resource $D:lang/$L.lang
     *
     * @param resourceManager the resource manager
     * @param languageList the language list
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

    /* (non-Javadoc)
     * @see net.minecraft.client.resources.Locale#isUnicode()
     */
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
     *
     * @param translateKey the translate key
     * @param parameters the parameters
     * @return the string
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

    /* (non-Javadoc)
     * @see net.minecraft.client.resources.Locale#hasKey(java.lang.String)
     */
    @Override
    public boolean hasKey(String key)
    {
        return this.properties.containsKey(key);
    }

    class DomainComparator implements Comparator<String>
    {
        
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
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
