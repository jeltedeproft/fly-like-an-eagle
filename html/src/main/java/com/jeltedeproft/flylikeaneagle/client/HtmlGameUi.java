package com.jeltedeproft.flylikeaneagle.client;

import com.jeltedeproft.flylikeaneagle.FlyLikeAnEagle;

final class HtmlGameUi implements FlyLikeAnEagle.Ui {
    @Override public void update(String state) {updateNative(state);}

    private static native void updateNative(String state) /*-{
        var values = {}, fields = state.split('|');
        for (var i = 1; i < fields.length; i++) {
            var split = fields[i].indexOf('=');
            if (split > 0) values[fields[i].slice(0, split)] = fields[i].slice(split + 1);
        }
        function element(id) { return $doc.getElementById(id); }
        function visible(id, yes, display) { var e=element(id); if(e)e.style.display=yes?(display||'block'):'none'; }
        function text(id, value) { var e=element(id); if(e)e.textContent=value; }
        function upgrade(value) { var p=(value||'').split('/'); return p[1]==='MAX'?'Lv '+p[0]+' MAX':'Lv '+p[0]+' - '+p[1]+' pts'; }
        function part(value) { return value==='OWNED'?'OWNED':(value||'').replace('BUY ','')+' pts'; }
        var screen=values.STATE, result=screen==='RESULT', shop=screen==='SHOP', running=!result&&!shop;
        text('meters',(values.DIST||0)+' m');
        var points=element('points'); if(points&&points.firstChild)points.firstChild.nodeValue=(values.POINTS||0)+' pts';
        text('best','Best '+(values.BEST||0)+' m');
        text('garageGold',(values.POINTS||0)+' GOLD');
        ['speed','glide','control','ramp','aero','bounce','slide'].forEach(function(id){text(id,upgrade(values[id.toUpperCase()]));});
        text('wings',part(values.WINGS)); text('susp',part(values.SUSP)); text('tail',part(values.TAIL)); text('booster',part(values.BOOSTER));
        visible('result',result,'flex'); visible('shop',shop); visible('garageLabel',shop);
        visible('left',running); visible('right',running); visible('hint',running); visible('meters',running);
        if(result){text('landed',(values.LAND==='-1'?0:values.LAND)+' m');text('reward','+'+(values.REWARD||0)+' points earned');text('outcome',values.OUTCOME==='CLEAN'?'Clean landing bonus included!':'Distance and pickups added to your total');}
    }-*/;
}
