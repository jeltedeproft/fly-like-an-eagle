package com.jeltedeproft.flylikeaneagle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** Scene2D UI backed entirely by a LibGDX Skin. */
final class GameHud implements Disposable {
    interface Actions {
        void openShop(); void launch(); void buyUpgrade(int index); void buyPart(int index); void advance();
    }

    private final Stage stage = new Stage(new ScreenViewport());
    private final Skin skin = new Skin();
    private final Table hud = new Table(), result = new Table(), shop = new Table();
    private final Label distance, points, resultDistance, reward, model, development;
    private final TextButton[] upgrades = new TextButton[7], parts = new TextButton[4];
    private final TextButton advance;

    GameHud(final Actions actions) {
        Texture shipUi = new Texture(Gdx.files.internal("Pixel Airship/Pixel Airship/09_UI/Ship_Interface/Ship_Interface_UI_Elements.png"));
        Texture tacticalUi = new Texture(Gdx.files.internal("Pixel Airship/Pixel Airship/09_UI/Tactical_UI/Tactical_UI_Elements.png"));
        shipUi.setFilter(Texture.TextureFilter.Nearest,Texture.TextureFilter.Nearest); tacticalUi.setFilter(Texture.TextureFilter.Nearest,Texture.TextureFilter.Nearest);
        skin.add("ship-ui-texture",shipUi); skin.add("tactical-ui-texture",tacticalUi);
        skin.add("window",new NinePatchDrawable(new NinePatch(new TextureRegion(shipUi,16,160,196,144),12,12,12,12)),Drawable.class);
        skin.add("panel",new NinePatchDrawable(new NinePatch(new TextureRegion(shipUi,240,160,132,82),8,8,8,8)),Drawable.class);
        skin.add("button",new NinePatchDrawable(new NinePatch(new TextureRegion(shipUi,128,32,32,16),6,6,5,5)),Drawable.class);
        skin.add("button-down",new NinePatchDrawable(new NinePatch(new TextureRegion(shipUi,164,32,16,16),5,5,5,5)),Drawable.class);
        skin.add("button-over",new NinePatchDrawable(new NinePatch(new TextureRegion(shipUi,184,32,16,16),5,5,5,5)),Drawable.class);
        skin.add("hud",new NinePatchDrawable(new NinePatch(new TextureRegion(tacticalUi,16,16,438,80),36,24,20,20)),Drawable.class);
        BitmapFont font = new BitmapFont(Gdx.files.internal("ui-font.fnt"));
        font.getRegion().getTexture().setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest, com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest);
        skin.add("default-font", font);
        skin.add("default", new Label.LabelStyle(font, new Color(.94f,.85f,.63f,1)));
        TextButton.TextButtonStyle button = new TextButton.TextButtonStyle();
        button.up=skin.getDrawable("button"); button.down=skin.getDrawable("button-down"); button.over=skin.getDrawable("button-over"); button.font=font; button.fontColor=new Color(.99f,.88f,.58f,1); button.downFontColor=Color.WHITE;
        skin.add("default", button);

        Table root = new Table(); root.setFillParent(true); stage.addActor(root);
        Table hudLayer = new Table();
        hudLayer.setFillParent(true);
        hudLayer.top().padTop(12);
        hud.setBackground(skin.getDrawable("hud"));
        hud.pad(18, 42, 18, 42);
        hudLayer.add(hud).width(720).height(96);
        stage.addActor(hudLayer);
        distance = new Label("0 m",skin); distance.setFontScale(1.7f);
        points = new Label("0 pts",skin); points.setAlignment(2);
        hud.add(distance).expandX().left(); hud.add(points).expandX().right();

        result.setBackground(skin.getDrawable("window")); result.pad(24); result.setVisible(false);
        Label done=new Label("RUN COMPLETE",skin); done.setFontScale(1.5f); result.add(done).row();
        resultDistance=new Label("0 m",skin); resultDistance.setFontScale(2.5f); result.add(resultDistance).pad(12).row();
        reward=new Label("+0 points",skin); result.add(reward).padBottom(14).row();
        TextButton open=new TextButton("OPEN UPGRADES",skin); open.addListener(click(actions::openShop)); result.add(open).width(260).height(54);
        root.add(result).center();

        shop.setBackground(skin.getDrawable("window")); shop.pad(18); shop.setVisible(false);
        Table shopRoot=new Table(); shopRoot.setFillParent(true); shopRoot.right().pad(16); shopRoot.add(shop).width(620).expandY().fillY(); stage.addActor(shopRoot);
        model=new Label("GEN 1 AIRSHIP",skin); model.setFontScale(1.35f); shop.add(model).colspan(4).left().row();
        development=new Label("DEVELOPMENT",skin); shop.add(development).colspan(4).left().padBottom(10).row();
        String[] upgradeNames={"SPEED","GLIDE","CONTROL","RAMP","AERO","BOUNCE","BRAKES"};
        for(int i=0;i<upgrades.length;i++){final int index=i;upgrades[i]=new TextButton(upgradeNames[i],skin);upgrades[i].addListener(click(()->actions.buyUpgrade(index)));shop.add(upgrades[i]).width(140).height(62).pad(4);if(i%4==3)shop.row();}
        shop.add().row();
        String[] partNames={"WINGS","GEAR","TAIL","ENGINE"};
        for(int i=0;i<parts.length;i++){final int index=i;parts[i]=new TextButton(partNames[i],skin);parts[i].addListener(click(()->actions.buyPart(index)));shop.add(parts[i]).width(140).height(62).pad(4);}shop.row();
        advance=new TextButton("NEXT GENERATION",skin);advance.addListener(click(actions::advance));shop.add(advance).colspan(4).width(300).height(58).padTop(10).row();
        TextButton launch=new TextButton("LAUNCH",skin);launch.addListener(click(actions::launch));shop.add(launch).colspan(4).width(300).height(58).padTop(8);
    }

    private ClickListener click(final Runnable action){return new ClickListener(){@Override public void clicked(InputEvent event,float x,float y){action.run();}};}

    void update(String encoded){ObjectMap<String,String> v=new ObjectMap<String,String>();String[] fields=encoded.split("\\|");for(int i=1;i<fields.length;i++){int p=fields[i].indexOf('=');if(p>0)v.put(fields[i].substring(0,p),fields[i].substring(p+1));}String state=v.get("STATE","");hud.setVisible(!"RESULT".equals(state)&&!"SHOP".equals(state));result.setVisible("RESULT".equals(state));shop.setVisible("SHOP".equals(state));distance.setText(v.get("DIST","0")+" m");points.setText(v.get("POINTS","0")+" pts\nBest "+v.get("BEST","0")+" m");resultDistance.setText(v.get("LAND","0")+" m");reward.setText("+"+v.get("REWARD","0")+" points");model.setText("GEN "+v.get("GEN","1")+"  "+v.get("MODEL","AIRSHIP"));development.setText("STATS "+v.get("DEV","0/28"));String[] keys={"SPEED","GLIDE","CONTROL","RAMP","AERO","BOUNCE","SLIDE"};for(int i=0;i<keys.length;i++)upgrades[i].setText(keys[i]+"\n"+v.get(keys[i],""));String[] pkeys={"WINGS","SUSP","TAIL","BOOSTER"};String[] names={"WINGS","GEAR","TAIL","ENGINE"};for(int i=0;i<pkeys.length;i++)parts[i].setText(names[i]+"\n"+v.get(pkeys[i],""));advance.setDisabled(!"true".equals(v.get("CANADVANCE","false")));}
    void render(float delta){stage.act(delta);stage.draw();}
    void resize(int width,int height){stage.getViewport().update(width,height,true);}
    InputProcessor input(){return stage;}
    @Override public void dispose(){stage.dispose();skin.dispose();}
}
