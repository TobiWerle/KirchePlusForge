package fr.harmonia.tsclientquery.answer;

import fr.harmonia.tsclientquery.objects.ParsedObject;

public class ChannelDescriptionAnswer extends Answer{

    public ChannelDescriptionAnswer(ParsedObject obj) {
        super(obj);
    }

    public String getChannelDescription() {
        return get("channel_description");
    }
}
