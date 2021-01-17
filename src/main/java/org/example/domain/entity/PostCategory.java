package org.example.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class PostCategory {

    @NonNull
    private int categoryId;

    @NonNull
    private String categoryName;
}
