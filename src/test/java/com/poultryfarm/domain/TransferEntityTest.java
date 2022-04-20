package com.poultryfarm.domain;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransferEntityTest {

    @Test
    void writeToOutputStream() {
        var entity = new TransferEntity(10, 20, 30, 40, "Cat");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        entity.writeToOutputStream(out);
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        try {
            int x = in.readInt();
            int y = in.readInt();
            int dx = in.readInt();
            int dy = in.readInt();
            String type = in.readUTF();
            assertEquals(10, x);
            assertEquals(20, y);
            assertEquals(30, dx);
            assertEquals(40, dy);
            assertEquals("Cat", type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void readFromInputStream() {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteOut);
        try {
            out.writeInt(10);
            out.writeInt(20);
            out.writeInt(30);
            out.writeInt(40);
            out.writeUTF("Cat");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var entity = new TransferEntity();
        ByteArrayInputStream in = new ByteArrayInputStream(byteOut.toByteArray());
        entity.readFromInputStream(in);
        assertEquals(10, entity.getX());
        assertEquals(20, entity.getY());
        assertEquals(30, entity.getDx());
        assertEquals(40, entity.getDy());
        assertEquals("Cat", entity.getType());
    }

    @Test
    void writeToWriter() {
        var entity = new TransferEntity(10, 20, 30, 40, "Cat");
        StringWriter writer = new StringWriter();
        entity.writeToWriter(writer);
        String[] args = writer.toString().split(" ");
        assertEquals(10, Integer.parseInt(args[0]));
        assertEquals(20, Integer.parseInt(args[1]));
        assertEquals(30, Integer.parseInt(args[2]));
        assertEquals(40, Integer.parseInt(args[3]));
        assertEquals("Cat", args[4].replaceAll("\n", ""));
    }

    @Test
    void readFromReader() {
        String line = "10 20 30 40 Cat\n";
        StringReader reader = new StringReader(line);
        TransferEntity entity = new TransferEntity();
        entity.readFromReader(reader);
        assertEquals(10, entity.getX());
        assertEquals(20, entity.getY());
        assertEquals(30, entity.getDx());
        assertEquals(40, entity.getDy());
        assertEquals("Cat", entity.getType());
    }
}