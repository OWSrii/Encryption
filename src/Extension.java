public enum Extension {
    JAR (".jar", (char) 1),
    ENCRYPTED(".encrypted", (char) 2),
    MP4(".mp4", (char) 3),
    PNG(".png", (char) 4),
    JPG(".jpg", (char) 5),
    TXT(".txt", (char) 6),
    PY(".py", (char) 7),
    MP3(".mp3", (char) 8),
    JAVA(".java", (char) 9),
    CLASS(".class", (char) 10),
    MTM(".mangetesmorts", (char) 11),
    PTDR(".ptdr", (char) 12),
    DECRYPTED(".decrypted", (char) 13);

    public String name;
    public char hash;

    Extension(String name, char hash) {
        this.name = name;
        this.hash = hash;
    }

    public String toString() {
        return this.name;
    }
}
