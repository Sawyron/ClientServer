package com.poultryfarm.services;

import com.poultryfarm.domain.GraphicEntity;
import com.poultryfarm.domain.ImageGraphicEntity;
import com.thoughtworks.xstream.XStream;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class XmlSerializer implements GraphicEntitySerializer {
    private XStream xStream = new XStream();

    public XmlSerializer() {
        xStream.allowTypes(new Class[]{ImageGraphicEntity.class});
        xStream.omitField(ImageGraphicEntity.class, "image");
    }

    public XmlSerializer(XStream xStream) {
        this.xStream = xStream;
    }

    @Override
    public void saveEntities(Collection<GraphicEntity> entities, String path) {
        String xml = xStream.toXML(entities);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<GraphicEntity> loadEntities(String path) {
        return (List<GraphicEntity>) xStream.fromXML(path);
    }
}
