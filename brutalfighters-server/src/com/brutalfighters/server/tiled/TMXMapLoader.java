package com.brutalfighters.server.tiled;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * TMX地图加载器
 *
 */
public class TMXMapLoader {
	private static SAXParserFactory factory;
	private static SAXParser saxParser;
	private static TMXHandler tmxhandler;
	
	public static void Load() {
		try {
			factory = SAXParserFactory.newInstance();
			tmxhandler = new TMXHandler();
			saxParser = factory.newSAXParser();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从文件中读取地图
	 * @param path	文件路径
	 * @return
	 */
	public static TiledMap readMap(String path) {
		try {
			saxParser.parse(new File(path), tmxhandler);
			return tmxhandler.getMap();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Couldn't read the map"); //$NON-NLS-1$
		return null;
	}
}
