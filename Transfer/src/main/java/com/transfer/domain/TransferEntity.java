package com.transfer.domain;

import java.io.*;

public class TransferEntity implements Serializable {
    private int x;
    private int y;
    private int dx;
    private int dy;
    private String type;

    public TransferEntity() {
        type = "";
    }

    public TransferEntity(int x, int y, int dx, int dy, String type) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.type = type;
    }

    public void writeToOutputStream(OutputStream stream) {
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeInt(x);
            out.writeInt(y);
            out.writeInt(dx);
            out.writeInt(dy);
            out.writeUTF(type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFromInputStream(InputStream stream) {
        DataInputStream in = new DataInputStream(stream);
        try {
            x = in.readInt();
            y = in.readInt();
            dx = in.readInt();
            dy = in.readInt();
            type = in.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToWriter(Writer writer) {
        try {
            writer.write(Integer.toString(x));
            writer.write(' ');
            writer.write(Integer.toString(y));
            writer.write(' ');
            writer.write(Integer.toString(dx));
            writer.write(' ');
            writer.write(Integer.toString(dy));
            writer.write(' ');
            writer.write(type);
            writer.write('\n');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFromReader(Reader reader) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            int charCode;
            while ((charCode = reader.read()) != -1) {
                char c = (char) charCode;
                if (c == '\n') {
                    break;
                }
                stringBuilder.append(c);
            }
            String[] args = stringBuilder.toString().split(" ");
            if (args.length >= 5) {
                x = Integer.parseInt(args[0]);
                y = Integer.parseInt(args[1]);
                dx = Integer.parseInt(args[2]);
                dy = Integer.parseInt(args[3]);
                type = args[4].replaceAll("\n", "");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransferEntity that = (TransferEntity) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (dx != that.dx) return false;
        if (dy != that.dy) return false;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + dx;
        result = 31 * result + dy;
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TransferEntity{" +
                "x=" + x +
                ", y=" + y +
                ", dx=" + dx +
                ", dy=" + dy +
                ", type='" + type + '\'' +
                '}';
    }
}
