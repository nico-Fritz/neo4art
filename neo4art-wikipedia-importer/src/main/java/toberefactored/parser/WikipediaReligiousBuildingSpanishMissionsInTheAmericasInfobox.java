/**
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package toberefactored.parser;

import java.util.Map;

import org.neo4art.domain.Coordinates;
import org.neo4art.domain.ReligiousBuilding;
import org.neo4art.importer.wikipedia.parser.util.WikipediaInfoboxParserUtils;

import toberefactored.parser.util.InfoboxParserUtil;
import toberefactored.parser.util.InfoboxUrlParser;

/**
 * 
 * @author Mattia Zaratin
 * @since 30 Apr 2015
 */
public class WikipediaReligiousBuildingSpanishMissionsInTheAmericasInfobox
{
  public static final String NAME   = "name";
  public static final String LATD   = "lat_degrees";
  public static final String LATM   = "lat_minutes";
  public static final String LATS   = "lat_seconds";
  public static final String LATNS  = "lat_direction";
  public static final String LONGD  = "long_degrees";
  public static final String LONGM  = "long_minutes";
  public static final String LONGS  = "long_seconds";
  public static final String LONGEW = "long_direction";
  public static final String IMAGE  = "image";
  public static final String STYLE  = "infobox";

  /**
   * 
   * @param text
   * @return
   */
  public static ReligiousBuilding parse(String text)
  {
    ReligiousBuilding spanishMissionsInTheAmericans = new ReligiousBuilding();
    Coordinates coordinates = new Coordinates();

    Map<String, String> map = WikipediaInfoboxParserUtils.asMap(text);

    for (String key : map.keySet())
    {
      switch (key)
      {
        case NAME:
          spanishMissionsInTheAmericans.setName(InfoboxParserUtil.removeAllParenthesis(map.get(key)));
          break;
        case IMAGE:
          spanishMissionsInTheAmericans.setImage(InfoboxUrlParser.infoboxUrl(map.get(key)));
          break;
        case LATD:
          coordinates.setLatD(Double.parseDouble(map.get(key)));
          break;
        case LATM:
          coordinates.setLatM(Double.parseDouble(map.get(key)));
          break;
        case LATS:
          coordinates.setLatS(Double.parseDouble(map.get(key)));
          break;
        case LATNS:
          coordinates.setLatNS(map.get(key));
          break;
        case LONGD:
          coordinates.setLongD(Double.parseDouble(map.get(key)));
          break;
        case LONGM:
          coordinates.setLongM(Double.parseDouble(map.get(key)));
          break;
        case LONGS:
          coordinates.setLongS(Double.parseDouble(map.get(key)));
          break;
        case LONGEW:
          coordinates.setLongEW(map.get(key));
          break;
      }

      spanishMissionsInTheAmericans.setCoordinates(coordinates);
    }
    
    return spanishMissionsInTheAmericans;
  }
}
