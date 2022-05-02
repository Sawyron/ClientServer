package com.transfer.serializers;

import com.transfer.domain.TransferEntity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntitySerializerTest {

    @ParameterizedTest
    @MethodSource("serializerStream")
    void saveAndRead(EntitySerializer serializer) {
        List<TransferEntity> entities = Arrays.asList(
                new TransferEntity(10, 20, 30, 40, "Cat"),
                new TransferEntity(15, 25, 35, 45, "Dog"),
                new TransferEntity(20, 30, 40, 50, "Bird")
        );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serializer.saveEntities(entities, out);
        List<TransferEntity> loadedEntities = serializer.loadEntities(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(entities.size(), loadedEntities.size());
        for (int i = 0; i < loadedEntities.size(); i++) {
            assertEquals(entities.get(i), loadedEntities.get(i));
        }
    }

    static Stream<EntitySerializer> serializerStream() {
        return Stream.of(
                new BinarySerializer(),
                new TextEntitySerializer(),
                new XmlSerializer()
        );
    }
}