package com.nagirescue;

import android.app.FragmentManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by cipher1729 on 10/10/2015.
 */

    public class MultiPartHelper {

        public String contentType;
        public String boundary;
        public HttpURLConnection connection;
        public static StringBuilder sb;
        private final List<FilePart> fileParts = new ArrayList<>();
        private final List<StringPart> stringParts = new ArrayList<>();

        public MultiPartHelper(HttpURLConnection connection) throws IOException {
            this(connection, "multipart/form-data");
        }

        public MultiPartHelper(HttpURLConnection connection, String contentType) throws IOException {
            this.boundary = String.valueOf(System.currentTimeMillis());
            this.connection = connection;
            contentType = contentType == null? "multipart/form-data": contentType;
            contentType += "; boundary=" + boundary;

            connection.setRequestProperty("Content-Type", contentType);
        }

        public void addStringPart(String data, String name) {
            stringParts.add(new StringPart(data, name));
        }

        public void addFilePart(File file, String contentType, String name) {
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            fileParts.add(new FilePart(file, contentType, name));
        }

        public void makeRequest() {
            Writer writer = null;
            try {
                OutputStream out = connection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(out));

                for (StringPart sp: stringParts) {
                    writer.write("--" + boundary + "\r\n");
                    writer.write("Content-Disposition: form-data; name=" + sp.name + "\r\n");
                    writer.write("Content-Type: text/plain\r\n\r\n");
                    writer.write(sp.data + "\r\n");
                }
                writer.flush();

                InputStream in = null;
                for (FilePart fp: fileParts) {
                    writer.write("--" + boundary + "\r\n");
                    writer.write("Content-Disposition: form-data; name="
                            + fp.name + "; filename=" + fp.file.getName() + "\r\n");
                    writer.write("Content-Type: " + fp.contentType + "\r\n\r\n");
                    writer.flush();

                    in = new FileInputStream(fp.file);
                    int count;
                    byte[] buffer = new byte[2048];
                    while ((count = in.read(buffer)) != -1) {
                        out.write(buffer, 0, count);
                    }
                    out.flush();
                    writer.write("\r\n");
                    writer.flush();

                    in.close();
                }
                writer.write("--" + boundary + "--\r\r");
                writer.flush();
                int responseCode = connection.getResponseCode();

                InputStream is = connection.getInputStream();
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String line;
                sb = new StringBuilder();
                line= bufferedReader.readLine();
                while(line!=null)
                {
                    sb.append(line);
                    line= bufferedReader.readLine();
                }

                is.close();


                out.close();
                writer.close();







            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        class StringPart {
            String data;
            String name;
            public StringPart(String data, String name) {
                this.data = data;
                this.name = name;
            }
        }

        class FilePart {
            File file;
            String name;
            String contentType;
            public FilePart(File file, String contentType, String name) {
                this.file = file;
                this.name = name;
                this.contentType = contentType;
            }
        }
    }

