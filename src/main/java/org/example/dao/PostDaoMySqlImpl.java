package org.example.dao;

import org.example.aspect.Cached;
import org.example.domain.entity.Post;
import org.example.domain.entity.PostCategory;
import org.example.domain.entity.PostTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class PostDaoMySqlImpl implements PostDao {


    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public PostDaoMySqlImpl(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public RowMapper<Post> postRowMapper = (resultSet, i) -> {
        Post post = new Post();
        post.setId(resultSet.getInt("id"));
        post.setPostAuthor(resultSet.getString("postAuthor"));
        post.setPublicationDate(resultSet.getString("publicationDate"));
        post.setPostName(resultSet.getString("postName"));
        post.setPostTheme(resultSet.getString("postTheme"));
        return post;
    };

    @Transactional(readOnly = true)
    @Override
    public PostTransfer findPostDatabase(PostTransfer postTransfer) {
        int startPosition = postTransfer.getStartPosition();

        List<Post> postList = jdbcTemplate.query("select * from posts where draft = 'no' limit ?, 3;", postRowMapper, startPosition);
        PostTransfer postTransfer1 = new PostTransfer();
        postTransfer1.setPostList(postList);
        postTransfer1.setCountLines(countLines());
        postTransfer1.setStartPosition(startPosition);
        postTransfer1.setPostPosition(startPosition + postList.size());
        return postTransfer1;
    }

    @Transactional(readOnly = true)
    @Override
    public Integer countLines() {
        List<Post> postList = jdbcTemplate.query("select * from posts where draft = 'no';", postRowMapper);
        return postList.size();
    }

    public PreparedStatementCreator insertPostIfNoData(Post post) {
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into posts (postAuthor, postName, postTheme, post, draft, categoryId) values(?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, post.getPostAuthor());
            preparedStatement.setString(2, post.getPostName());
            preparedStatement.setString(3, post.getPostTheme());
            preparedStatement.setString(4, post.getPostBody());
            preparedStatement.setString(5, post.getDraft());
            preparedStatement.setInt(6, post.getCategoryId());
            return preparedStatement;
        };
    }

    public PreparedStatementCreator insertPost(Post post) {
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into posts (postAuthor, publicationDate ,postName, postTheme, post, draft, categoryId) values (?,?,?,?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, post.getPostAuthor());
            preparedStatement.setString(2, post.getPublicationDate());
            preparedStatement.setString(3, post.getPostName());
            preparedStatement.setString(4, post.getPostTheme());
            preparedStatement.setString(5, post.getPostBody());
            preparedStatement.setString(6, post.getDraft());
            preparedStatement.setInt(7, post.getCategoryId());
            return preparedStatement;
        };
    }

    @Transactional
    @Override
    public Integer save(Post data) throws SQLException {
        Post post = data;
        List<PostCategory> postCategoryList = selectAllCategories();
        for (int i = 0; i < postCategoryList.size(); i++) {
            if (postCategoryList.get(i).getCategoryName().equals(post.getPostCategory())) {
                post.setCategoryId(postCategoryList.get(i).getCategoryId());
            }
        }

        if (post.getPublicationDate().equals("")) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(insertPostIfNoData(data), keyHolder);
            return keyHolder.getKey().intValue();

        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(insertPost(data), keyHolder);
            return keyHolder.getKey().intValue();
        }
    }

    @Override
    public List<Post> findAll() throws SQLException {
        return null;
    }

    public RowMapper<Post> fullPostRowMapper = (resultSet, i) -> {
        Post post = new Post();
        post.setPostAuthor(resultSet.getString("postAuthor"));
        post.setPublicationDate(resultSet.getString("publicationDate"));
        post.setPostName(resultSet.getString("postName"));
        post.setPostTheme(resultSet.getString("postTheme"));
        post.setPostBody(resultSet.getString("post"));
        post.setDraft(resultSet.getString("draft"));
        post.setId(resultSet.getInt("id"));
        return post;
    };

    @Cached
    @Transactional(readOnly = true)
    @Override
    public Post findPostById(Integer id) throws SQLException {
        return jdbcTemplate.queryForObject("select * from posts where id=?", fullPostRowMapper, id);
    }

    @Override
    public void delete(Integer integer) throws SQLException {

    }

    @Override
    public Post update(Post data) throws SQLException {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    @Cached
    public PostCategory findPostCategoryById(Integer id) {
        return jdbcTemplate.queryForObject("select * from postCategory where categoryId = ? ;", postCategoryRowMapper, id);
    }

    public RowMapper<PostCategory> postCategoryRowMapper = (resultSet, i) -> {
        PostCategory postCategory = new PostCategory();
        postCategory.setCategoryId(resultSet.getInt("categoryId"));
        postCategory.setCategoryName(resultSet.getString("categoryName"));
        return postCategory;
    };


    public PreparedStatementCreator insertPostCategory(PostCategory postCategory) {
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into postCategory(categoryName) values (?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, postCategory.getCategoryName());
            return preparedStatement;
        };
    }

    @Transactional
    @Override
    public Integer saveCategory(PostCategory postCategory) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(insertPostCategory(postCategory), keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Transactional
    @Override
    public void deleteCategory(Integer id) throws SQLException {
        jdbcTemplate.update("delete from postCategory where categoryId=?", id);
    }


    @Transactional(readOnly = true)
    @Override
    public List<PostCategory> selectAllCategories() {
        return jdbcTemplate.query("select * from postCategory", postCategoryRowMapper);
    }


}
