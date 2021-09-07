package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.ChannelDescriptionAnswer;
import fr.harmonia.tsclientquery.objects.ParsedObject;

public class ChannelDescription extends Query<ChannelDescriptionAnswer>{

    public ChannelDescription() {
        super("channelvariable cid=227 channel_description");
    }

    @Override
    public void buildAnswer(ParsedObject obj) {
        answer = new ChannelDescriptionAnswer(obj);
    }
}
