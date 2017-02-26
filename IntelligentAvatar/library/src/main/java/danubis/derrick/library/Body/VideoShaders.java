package danubis.derrick.library.Body;

/**
 * DO NOT CHANGE THIS FILE
 */
class VideoShaders {

    static final String VERTEX_SHADER =
            "uniform mat4 uMVPMatrix;\n"
                    + "uniform mat4 uSTMatrix;\n"
                    + "attribute vec4 aPosition;\n"
                    + "attribute vec4 aTextureCoord;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "void main() {\n"
                    + "  gl_Position = uMVPMatrix * aPosition;\n"
                    + "  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n"
                    + "}\n";

    static final String FRAGMENT_SHADER =
            "#extension GL_OES_EGL_image_external : require \n"
                    + "precision mediump float; \n"
                    + "uniform samplerExternalOES texSamplerOES; \n"
                    + "varying vec2 vTextureCoord;\n"
                    + "void main() {\n"
                    + "  float thresholdSensitivity = 0.415; // [0, 1.732];\n"
                    + "  float smoothing = 0.1; // [0, 1]\n"
                    + "  vec3 colorToReplace = vec3(0, 1.00, 0);\n"
                    + "  vec4 input_color = texture2D(texSamplerOES, vTextureCoord);\n"
                    + "  float maskY = 0.2989 * colorToReplace.r + 0.5866 * colorToReplace.g + 0.1145 * colorToReplace.b;\n"
                    + "  float maskCr = 0.7132 * (colorToReplace.r - maskY);\n"
                    + "  float maskCb = 0.5647 * (colorToReplace.b - maskY);\n"
                    + "  float Y = 0.2989 * input_color.r + 0.5866 * input_color.g + 0.1145 * input_color.b;\n"
                    + "  float Cr = 0.7132 * (input_color.r - Y);\n"
                    + "  float Cb = 0.5647 * (input_color.b - Y);\n"
                    + "  float d = distance(vec2(Cr, Cb), vec2(maskCr, maskCb));\n"
                    + "  float blendValue = smoothstep(thresholdSensitivity, thresholdSensitivity+smoothing, d);\n"
                    + "  gl_FragColor = vec4(input_color.rgb * blendValue, 1.0 * blendValue);\n"
                    + "}\n";
}