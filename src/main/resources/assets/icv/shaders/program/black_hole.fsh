#version 150

#moj_import <lodestone:common_math.glsl>

// Samplers
uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;
// Multi-Instance uniforms
uniform samplerBuffer DataBuffer;
uniform int InstanceCount;
// Matrices needed for world position calculation
uniform mat4 invProjMat;
uniform mat4 invViewMat;

uniform vec2 InSize;
uniform vec2 OutSize;

uniform float Time;

uniform vec3 cameraPos;

in vec2 texCoord;
out vec4 fragColor;

bool skip = false;

vec2 worldPosToTexCoord(vec3 worldPos, mat4 invViewMat, mat4 invProjMat, vec3 cameraPos) {
    mat4 viewMat = inverse(invViewMat);
    mat4 projMat = inverse(invProjMat);
    vec3 localPos = worldPos - cameraPos;

    vec4 clipSpace = projMat * viewMat * vec4(localPos, 1.0);
    vec4 ndc = clipSpace / clipSpace.w;

    if (clipSpace.w <= 0.0) {
        skip = true;
    }

    return ndc.xy * 0.5 + 0.5;
}

void main() {

    //setting up the screen to then distort it after
    vec4 diffuseColor = vec4(texture(DiffuseSampler, texCoord));
    vec3 worldPos = getWorldPos(MainDepthSampler,texCoord,invProjMat,invViewMat,cameraPos);
    fragColor = diffuseColor;

    for (int instance = 0; instance < InstanceCount; instance++) {

        //collecting instance, center, and color values
        int index = instance * 6;
        vec3 center = fetch3(DataBuffer, index);

        //getting the in-world distance to the center
        float distance = length(worldPos - center);

        //giving the shader a wobbly effect
        float time = pow(abs(((sin(3.1415*Time*2))) + 0.1), 2);
        float radius = 4 + time/6;

        vec3 toSphere = normalize(center - cameraPos);
        float cosMaxAngle = radius / length(center - cameraPos); // sphere angular radius

        // current pixel's ray direction
        vec3 rayDir = normalize(worldPos - cameraPos);

        bool cameraInside = length(cameraPos - center) < radius+2;
        // if rayDir is outside sphere cone, skip
        if (!cameraInside && dot(rayDir, toSphere) < cosMaxAngle) {
            // no chance to hit sphere → skip raymarch
            return;
        }

        float t = 0.0;
        float tMax = length(worldPos - cameraPos);    // max distance to march
        float dt = distance;      // step size
        bool hit = false;
        vec3 hitPoint;

        vec3 p = cameraPos;

        for (int i = 0; i < 20; i++) {

            float dist = length(p - center) - radius;  // SDF of sphere
            if (t > tMax) break; // safety

            if (dist < 0.001) {       // hit threshold
                hit = true;
                hitPoint = p;
                break;
            }

            t += dist;
            p += rayDir * dist;       // <-- adaptive step!
        }

        if (hit) {

            vec2 centerCoord = worldPosToTexCoord(center, invViewMat, invProjMat, cameraPos);

            float aspect = InSize.x / InSize.y;
            vec2 scaledTexCoord = vec2(texCoord.x, texCoord.y / aspect);
            vec2 scaledCenter   = vec2(centerCoord.x, centerCoord.y / aspect);

            distance = length(scaledTexCoord - scaledCenter);
            radius = (1.0 / length(center - cameraPos)) * t;

            vec2 warpedTex = texCoord;



            vec2 dir = texCoord - centerCoord;

            float distNorm = distance / radius;

            float pull = (1.0 - distNorm * distNorm);

            warpedTex += normalize(dir) * pull * 0.05;



            fragColor.rgb = texture(DiffuseSampler, warpedTex).rgb;

            vec3 color = vec3(1,0,0);
            float colorfalloff = pow(clamp(distance / radius, 0.0, 1.0), 3);
            fragColor.rgb *= (color*2 * (1-colorfalloff) + 1.0 + ((colorfalloff*-1.2)/2))/2;

        }
    }
}

