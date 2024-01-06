import views.screen.home.MediaHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FilterHelper {
    public static List<MediaHandler> filterMediaByKeyWord(String keyword, List<MediaHandler> mediaList) {
        List<MediaHandler> filteredItems = new ArrayList<>();
        for (MediaHandler mediaHandler : mediaList) {
            if (mediaHandler.getMedia().getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                filteredItems.add(mediaHandler);
            }
        }
        return filteredItems;
    }

    public static List<MediaHandler> filterMediaByMenuButton(String text, List<MediaHandler> mediaList) {
        List<MediaHandler> filteredItems = new ArrayList<>();
        mediaList.forEach(me -> {
            MediaHandler media = (MediaHandler) me;
            if (media.getMedia().getTitle().toLowerCase().startsWith(text.toLowerCase())){
                filteredItems.add(media);
            }else{
                if (text.equals("<20đ")) {
                    if (media.getMedia().getPrice() < 20) {
                        filteredItems.add(media);
                    }

                } else if (text.equals("20đ-50đ")) {
                    if (media.getMedia().getPrice() >= 20 && media.getMedia().getPrice() < 50) {
                        filteredItems.add(media);
                    }
                } else if (text.equals("50đ-100đ")) {
                    if (media.getMedia().getPrice() >= 50 && media.getMedia().getPrice() <= 100) {
                        filteredItems.add(media);
                    }
                }
                else if (text.equals(">100đ")) {
                    if (media.getMedia().getPrice() > 100) {
                        filteredItems.add(media);
                    }
                }

                Collections.sort(filteredItems, Comparator.comparingDouble(
                        mediax -> ((MediaHandler) mediax).getMedia().getPrice()));
            }

        });
        return filteredItems;
    }
}
