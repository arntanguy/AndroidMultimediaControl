package media;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Example metadata (for banshee) :
 * {u'mpris:artUrl': u
 * 'file:///home/arnaud/.cache/media-art/album-28e74852b80d9b6e632a56f9771c0801.jpg
 * ' , u'mpris:length': 261892000L, u'mpris:trackid':
 * u'/org/bansheeproject/Banshee/Track/552643', u'xesam:album': u'Viva Santana!
 * CD 2', u'xesam:albumArtist': [u'Santana'], u'xesam:artist': [u'Santana'],
 * u'xesam:genre': [u'Folk-Rock'], u'xesam:title': u'Brotherhood',
 * u'xesam:trackNumber': 1, u'xesam:url': u
 * 'file:///media/DATA/Musique/Santana%20Discografia%20Mp3%20320kbps/4%20Some%20Extra%20live,%20Bootlegs%20&%20Other%20albums/1988%20Viva%20Santana%20(Live)%20@320/CD2/01%20Brotherhood.mp3
 * ' }
 */
public class MetaDataMPRIS2 extends MetaData {
    private static final long serialVersionUID = 1L;

    public MetaDataMPRIS2(Map<String, String> metaDataMap) {
        super(metaDataMap);
    }

    @Override
    public String getLocation() {
        return metaData.get("xesam:url");
    }

    @Override
    public String getTitleFromLocation() {
        String url = null;
        try {
            url = URLDecoder.decode(getLocation(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return (url != null) ? url.substring(url.lastIndexOf('/') + 1,
                url.lastIndexOf('.')) : "";
    }

    @Override
    public int getLength() {
        String length = metaData.get("mpris:length");
        return (length != null) ? Integer.parseInt(length) : 0;
    }

    public String getCoverLocation() {
        return metaData.get("mpris:artUrl");
    }

    @Override
    public String getArtist() {
        return metaData.get("xesam:artist");
    }

    @Override
    public String getTitle() {
        return metaData.get("xesam:title");
    }
}
