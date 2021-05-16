package com.brutalfighters.server.tiled;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * TMX处理器
 *
 */
public class TMXHandler extends DefaultHandler {
	private TiledMap map;
	private int tilesetID;
	
	@Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
		if(qName.equalsIgnoreCase("map")) { 
			// Creates the map
			map = new TiledMap(Integer.parseInt(attributes.getValue("width")) - 1, 
					Integer.parseInt(attributes.getValue("height")) - 1, 
					Integer.parseInt(attributes.getValue("tilewidth")), 
					Integer.parseInt(attributes.getValue("tileheight")));
			// Adding the initial tileset, must have, because all `gid`'s start with 1, 0 being the initial tileset.
			// 必须添加初始瓦片集，因为所有`gid`均以1开头，0是初始图块集。
			map.addTileset(0); 
		} else if(qName.equalsIgnoreCase("stile")) { 
			// Adds tileset, will add properties later
			// 添加瓦片集，稍后再添加属性（瓦片集的id从1开始）
			tilesetID = Integer.parseInt(attributes.getValue("id")) + 1;
			map.addTileset(tilesetID);
		} else if(qName.equalsIgnoreCase("property")) { 
			// Adds property to the last tileset, because the latest is the current.
			// 将属性添加到最后一个瓦片集，因为最后那个是当前瓦片。
			map.editTileset(tilesetID, 
					attributes.getValue("name"), 
					attributes.getValue("value"));
		} else if(qName.equalsIgnoreCase("layer")) { 
			// Adds layer to the map, will add tiles later
			// 在地图上添加瓦片层，稍后将添加瓦片集
			map.addTiledLayer(Integer.parseInt(attributes.getValue("width")), 
					Integer.parseInt(attributes.getValue("height")));
		} else if(qName.equalsIgnoreCase("tile")) { 
			// Adds tiles into the map, into the last layer, because the latest is the current.
			// 将图块添加到地图的最后一层，因为最后那个是当前图层。
			map.addTile(map.getTiledLayersLength() - 1, 
					Integer.parseInt(attributes.getValue("gid")));
		}
	}
	
	@Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
		// Nothing here for now
	}
	
	@Override
    public void characters(char ch[], int start, int length) throws SAXException {
		// Nothing here for now
	}
	
	public TiledMap getMap() {
		return map;
	}
}
