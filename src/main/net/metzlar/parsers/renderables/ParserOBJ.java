package net.metzlar.parsers.renderables;

import net.metzlar.JRay;
import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.renderable.Renderable;
import net.metzlar.renderEngine.types.Vec2;
import net.metzlar.renderEngine.types.Vec3;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParserOBJ implements Parser<Renderable> {
    List<Vec3> vertices = new ArrayList<>();
    List<Vec2> textureCoordinates = new ArrayList<>();
    List<Vec3> vertexNormals = new ArrayList<>();

    @Override
    public Renderable parse(Element docElement) {
        String path = docElement.attr("path");

        File objectFile = new File(JRay.MODEL_DIRECTORY, path);
        File materialFile = new File(JRay.MODEL_DIRECTORY, path.replace(".obj", ".mtl"));

        try {
            BufferedReader reader = new BufferedReader(new FileReader(objectFile));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.charAt(0) == '#') continue;

                String[] split = line.split(" ");

                switch (split[0]) {
                    case "v": // Vertex
                        vertices.add(parseVertex(split));

                    case "vt": // Texture coordinates
                        textureCoordinates.add(parseTextureCoordinate(split));

                    case "vn": // Vertex normal
                        vertexNormals.add(parseVertexNormal(split));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Vec3 parseVertex(String[] strings) {
        return new Vec3(
                Float.parseFloat(strings[1]),
                Float.parseFloat(strings[2]),
                Float.parseFloat(strings[3])
        );
    }

    private Vec2 parseTextureCoordinate(String[] strings) {
        return new Vec2(
                Float.parseFloat(strings[1]),
                Float.parseFloat(strings[2])
        );
    }

    private Vec3 parseVertexNormal(String[] strings) {
        return new Vec3(
                Float.parseFloat(strings[1]),
                Float.parseFloat(strings[2]),
                Float.parseFloat(strings[3])
        );
    }
}
