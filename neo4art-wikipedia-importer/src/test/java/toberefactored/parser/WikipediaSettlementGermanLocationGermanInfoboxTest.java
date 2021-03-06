package toberefactored.parser;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.neo4art.domain.Settlement;

public class WikipediaSettlementGermanLocationGermanInfoboxTest {

	private static String INFOBOX ="{{Infobox German location\n"+
			"|name               = Dortmund\n"+
			"|lat_deg            = 51 \n"+
			"| lat_min = 31 \n"+
			"| lat_sec = \n"+
			"|lon_deg            =  7\n"+ 
			"| lon_min = 28 \n"+
			"| lon_sec = \n"+
			"|website            = [http://www.dortmund.de/ www.dortmund.de]\n"+
			"}}";
	
	@Test
	public void shoudParseSettlement() throws MalformedURLException{
		Settlement settlement = WikipediaSettlementGermanLocationInfoboxParser.parse(INFOBOX);
		
		Assert.assertEquals("Dortmund", settlement.getName());
		Assert.assertEquals("51.0",""+settlement.getCoordinates().getLatD());
		Assert.assertEquals("31.0",""+settlement.getCoordinates().getLatM());
		Assert.assertEquals("7.0",""+settlement.getCoordinates().getLongD());
		Assert.assertEquals("28.0",""+settlement.getCoordinates().getLongM());
		Assert.assertEquals(new URL("http://www.dortmund.de/"), settlement.getWebsite());
	}
}
