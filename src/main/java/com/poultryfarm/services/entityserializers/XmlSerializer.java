package com.poultryfarm.services.entityserializers;

import com.poultryfarm.domain.ImageGraphicEntity;
import com.poultryfarm.domain.TransferEntity;
import com.thoughtworks.xstream.XStream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class XmlSerializer implements EntitySerializer {
    private XStream xStream = new XStream();

    public XmlSerializer() {
        xStream.allowTypes(new Class[]{ImageGraphicEntity.class});
        xStream.omitField(ImageGraphicEntity.class, "image");
    }

    public XmlSerializer(XStream xStream) {
        this.xStream = xStream;
    }

    @Override
    public void saveEntities(Collection<TransferEntity> entities, File file) {
        String xml = xStream.toXML(entities);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TransferEntity> loadEntities(File file) {
        return (List<TransferEntity>) xStream.fromXML(file);
    }
}
