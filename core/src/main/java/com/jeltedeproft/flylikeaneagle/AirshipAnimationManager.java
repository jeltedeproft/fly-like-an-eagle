package com.jeltedeproft.flylikeaneagle;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/** Atlas-backed animation cache following TowerOfLife's timer/getKeyFrame pattern. */
final class AirshipAnimationManager implements Disposable {
    private final TextureAtlas atlas;
    private final Animation<TextureRegion> airship;
    private float timer;

    AirshipAnimationManager(String atlasPath) {
        atlas = new TextureAtlas(atlasPath);
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions("airship");
        if (regions.size == 0) throw new IllegalStateException("No indexed 'airship' regions in " + atlasPath);
        Array<TextureRegion> frames = new Array<TextureRegion>(regions.size);
        for (TextureAtlas.AtlasRegion region : regions) frames.add(region);
        airship = new Animation<TextureRegion>(.9f / frames.size, frames, Animation.PlayMode.LOOP);
    }

    void update(float delta) { timer += delta; }
    void reset() { timer = 0; }
    TextureRegion getFrame() { return airship.getKeyFrame(timer); }
    @Override public void dispose() { atlas.dispose(); }
}
