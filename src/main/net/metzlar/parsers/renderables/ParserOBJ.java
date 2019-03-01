package net.metzlar.parsers.renderables;

import net.metzlar.JRay;
import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.renderable.Face;
import net.metzlar.renderEngine.scene.renderable.Model;
import net.metzlar.renderEngine.scene.renderable.Renderable;
import net.metzlar.renderEngine.types.Angle;
import net.metzlar.renderEngine.types.Vec2;
import net.metzlar.renderEngine.types.Vec3;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParserOBJ implements Parser<Renderable> {
    List<Vec3> allVertices = new ArrayList<>();
    List<Vec2> allTextureCoordinates = new ArrayList<>();
    List<Vec3> allVertexNormals = new ArrayList<>();
    List<TempFace> tempFaces = new ArrayList<>();

    @Override
    public Renderable parse(Element docElement) {
        Model model = null;

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
                        allVertices.add(parseVertex(split));
                        break;

                    case "vt": // Texture coordinates
                        allTextureCoordinates.add(parseTextureCoordinate(split));
                        break;

                    case "vn": // Vertex normal
                        allVertexNormals.add(parseVertexNormal(split));
                        break;

                    case "f": // TempFace
                        tempFaces.add(new TempFace(split));
                        break;
                }
            }

            model = new Model(
                    new Vec3(
                            Double.parseDouble(docElement.select("position > x").html()),
                            Double.parseDouble(docElement.select("position > y").html()),
                            Double.parseDouble(docElement.select("position > z").html())
                    ),
                    new Angle(
                            Double.parseDouble(docElement.select("orientation > p").html()),
                            Double.parseDouble(docElement.select("orientation > y").html()),
                            Double.parseDouble(docElement.select("orientation > r").html())
                    ),
                    null
            );

            for (TempFace tempFace : tempFaces) {
                model.addFaces(tempFace.triangulate());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return model;
    }

    private Vec3 parseVertex(String[] strings) {
        return new Vec3(
                Float.parseFloat(strings[1]),
                Float.parseFloat(strings[2]),
                Float.parseFloat(strings[3])
        ).multiply(20);
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

    class TempFace {
        List<Vec3> vertices = new ArrayList<>();
        List<Vec2> textureCoords = new ArrayList<>();
        List<Vec3> normals = new ArrayList<>();

        private TempFace(String[] strings) {
            parse(strings);
        }

        private void parse(String[] strings) {
            for (int i = 1; i < strings.length; i++) {
                String s = strings[i];

                String[] faceData = s.split("/");

                vertices.add(allVertices.get(Integer.valueOf(faceData[0]) - 1));
                if (faceData.length == 1) continue;
                textureCoords.add(allTextureCoordinates.get(Integer.valueOf(faceData[1]) - 1));
                if (faceData.length == 2) continue;
                normals.add(allVertexNormals.get(Integer.valueOf(faceData[2]) - 1));
            }
        }

        private List<Face> triangulate() {
            List<Face> returnList = new ArrayList<>();

            for (int i = 2; i < vertices.size(); i++) {
                Vec3[] vertices = new Vec3[3];
                Vec2[] textureCoords = new Vec2[3];
                Vec3[] normals = new Vec3[3];

                int[] indices = {0, i-1, i};

                for (int a = 0; a < 3; a++) {
                    int index = indices[a];

                    vertices[a] = this.vertices.get(index);

                    if (this.textureCoords.size() - 1 >= index)
                        textureCoords[a] = this.textureCoords.get(index);

                    if (this.normals.size() - 1 >= index)
                        normals[a] = this.normals.get(index);
                }

                returnList.add(new Face(vertices, textureCoords, normals));
            }

            return returnList;
        }
    }
}
