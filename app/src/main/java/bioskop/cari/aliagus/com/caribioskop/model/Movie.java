package bioskop.cari.aliagus.com.caribioskop.model;

import java.util.List;

/**
 * Created by ali on 11/02/18.
 */

public class Movie {
    String id;
    String voteAverage;
    String title;
    String popularity;
    String posterPath;
    String overView;
    String releaseDate;
    List<String> genresList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<String> getGenresList() {
        return genresList;
    }

    public void setGenresList(List<String> genresList) {
        this.genresList = genresList;
    }
}
