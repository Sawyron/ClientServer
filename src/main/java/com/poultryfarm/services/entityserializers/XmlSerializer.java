package com.poultryfarm.services.entityserializers;

import com.poultryfarm.domain.TransferEntity;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class XmlSerializer implements EntitySerializer {
    private final XStream xStream;

    public XmlSerializer() {
        xStream = new XStream();
        xStream.addPermission(NoTypePermission.NONE);
        xStream.addPermission(NullPermission.NULL);
        xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);
        xStream.allowTypes(new Class[]{TransferEntity.class, LinkedList.class});
    }

    public XmlSerializer(XStream xStream) {
        this.xStream = xStream;
    }

    @Override
    public void saveEntities(Collection<TransferEntity> entities, OutputStream out) {
        xStream.toXML(entities, out);
    }

    @Override
    public List<TransferEntity> loadEntities(InputStream in) {
        List<TransferEntity> entities;
        entities = (List<TransferEntity>) xStream.fromXML(in);
        if (entities == null) {
            entities = new LinkedList<>();
        }
        return entities;
    }
}
