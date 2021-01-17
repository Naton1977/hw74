package org.example.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Post {
    @NonNull
    private String postAuthor;
    @NonNull
    private String publicationDate;
    @NonNull
    private String postName;
    @NonNull
    private String postTheme;
    @NonNull
    private String postBody;
    @NonNull
    private String draft;
    @NonNull
    private int categoryId;
    @NonNull
    private String postCategory;

    private int id;
}
