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

package org.neo4art.colour.manager;

import java.io.IOException;
import java.util.List;

import org.neo4art.domain.Artwork;

/**
 * @author Lorenzo Speranzoni
 * @since 2 May 2015
 */
public interface VanGoghArtworksColourAnalysisManager
{
  /**
   * @param fileName
   * @return
   * @throws IOException
   */
  List<Artwork> loadArtworksFromFile(String fileName) throws IOException;

  /**
   * 
   * @param artworks
   */
  void computeAndSaveColourAnalyses(List<Artwork> artworks);
}
