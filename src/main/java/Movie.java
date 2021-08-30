public class Movie {
    private String director;
    private String title;
    private String description;

    public Movie(String director,String title){
        this.director = director;
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Movie) {
            Movie movie = (Movie) obj;
            if (movie.getDirector().equals(this.director) && movie.getTitle().equals(this.title)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        String key = this.director + this.title + this.description;
        return key.hashCode();
    }
}
