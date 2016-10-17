package com.zhongyi.hid.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.LobRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Repository;

import com.google.common.base.Joiner;
import com.google.common.io.Closeables;
import com.zhongyi.hid.service.MakeBundleContext.CmsSlide;
import com.zhongyi.hid.service.commands.CreateDocumentEntryCommand.CmsGenre;
import com.zhongyi.hid.util.JsonUtil;
import com.zhongyi.hid.util.WordPressImageCaptionEscapeReader;

@Repository
public class CmsDbRepository {
	
	private final String cmsTablePrefix = SystemContextListener.getSystemProperty().getTablePrefix();

	private final String sql_select_post = String.format(
			"select post_title,post_author,post_modified,post_excerpt,post_mime_type,post_content from %s_posts where ID=?", cmsTablePrefix);


	private final String sql_select_profile = String.format(
		"select meta_value as profile from %s_postmeta where post_id=? and meta_key='zy_thumb'",cmsTablePrefix);
	
	private final String sql_select_background = String.format(
			"select meta_value as background from %s_postmeta where post_id=? and meta_key='zy_background'",cmsTablePrefix);
	
	
	
	private final String sql_select_author = String.format("select user_nicename as author from  %s_users where ID=?", cmsTablePrefix);

	
	private final String sql_select_start_year = String.format(
		"select meta_value as start_year from %s_postmeta where post_id=? and meta_key='zy_start_year'",cmsTablePrefix);
	
	private final String sql_select_genre = String.format(
		"select  %s_terms.term_id as genre_id,%s_terms.name as genre_name from %s_term_relationships inner join %s_term_taxonomy on %s_term_relationships.term_taxonomy_id = %s_term_taxonomy.term_taxonomy_id inner join %s_terms on %s_term_taxonomy.term_id = %s_terms.term_id where %s_term_relationships.object_id = ? and %s_term_taxonomy.taxonomy='zy_genre'",cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix);
	
	private final String sql_select_city = String.format(
		"select %s_terms.name as city,%s_term_taxonomy.description as coordinate from %s_term_relationships inner join %s_term_taxonomy on %s_term_relationships.term_taxonomy_id = %s_term_taxonomy.term_taxonomy_id inner join %s_terms on %s_term_taxonomy.term_id = %s_terms.term_id where %s_term_relationships.object_id = ? and %s_term_taxonomy.taxonomy='zy_city'",cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix);
	
	private final String sql_select_artists = String.format(
		"select %s_terms.name as artist from %s_term_relationships inner join %s_term_taxonomy on %s_term_relationships.term_taxonomy_id = %s_term_taxonomy.term_taxonomy_id inner join %s_terms on %s_term_taxonomy.term_id = %s_terms.term_id where %s_term_relationships.object_id = ? and %s_term_taxonomy.taxonomy='zy_people'",cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix);
	
	private final String sql_select_organizations = String.format(
		"select %s_terms.name as organization from %s_term_relationships inner join %s_term_taxonomy on %s_term_relationships.term_taxonomy_id = %s_term_taxonomy.term_taxonomy_id inner join %s_terms on %s_term_taxonomy.term_id = %s_terms.term_id where %s_term_relationships.object_id = ? and %s_term_taxonomy.taxonomy='zy_company'",cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix,cmsTablePrefix);
	
	private final String sql_select_slide =String.format("select meta_key,meta_value from %s_postmeta where post_id = ",cmsTablePrefix);
	
	
	private final String sql_select_data_media = String.format("select meta_key,meta_value from %s_postmeta where meta_key in ", cmsTablePrefix);
	
	
	private final String sql_update_lock_status  = String.format("update %s_pack_ids set pack_lock=1 where post_id=?", cmsTablePrefix);

	
	
	private final String sql_insert_log = String.format("insert into %s_logs(type,level,message,log_time) values(?,?,?,?)",cmsTablePrefix);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("deprecation")
	public void readPost(final int postId, final MakeBundleContext.CmsPost cmsPost) {
		jdbcTemplate.execute(sql_select_post,new PreparedStatementCallback<Void>() {
				@Override
				public Void doInPreparedStatement(PreparedStatement ps)	throws SQLException, DataAccessException {
					ps.setLong(1, postId);
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						cmsPost.setTitle(rs.getString("post_title"));
						cmsPost.setPostDate(String.valueOf(rs.getDate("post_modified").getTime()));
						cmsPost.setAuthor(rs.getString("post_author"));
						cmsPost.setDescription(rs.getString("post_excerpt"));
						cmsPost.setPostType(rs.getString("post_mime_type"));
						InputStream inputStream = rs.getBinaryStream("post_content");
						Writer output = null;
						try {
							output = new OutputStreamWriter(new FileOutputStream(cmsPost.getContentTmpFile()),"UTF-8");
							IOUtils.copy(new WordPressImageCaptionEscapeReader(new InputStreamReader(inputStream)), output);
						} catch (IOException e) {
							throw new LobRetrievalFailureException(
									"read post_content from posts error", e);
						} finally {
							Closeables.closeQuietly(inputStream);
							Closeables.closeQuietly(output);
						}
					}
					return null;
				}
			});
	}
	
	
	public String queryForProfile(final int postId){
		return jdbcTemplate.execute(sql_select_profile, new PreparedStatementCallback<String>(){
			@Override
			public String doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setLong(1, postId);
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					return rs.getString("profile");
				}
				return null;
			}
		});
	}
	
	public String queryForBackground(final int postId){
		return jdbcTemplate.execute(sql_select_background, new PreparedStatementCallback<String>(){
			@Override
			public String doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setLong(1, postId);
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					return rs.getString("background");
				}
				return null;
			}
		});
	}
	
	
	
	public String queryForAuthor(final int userId){
		return jdbcTemplate.execute(sql_select_author, new PreparedStatementCallback<String>(){
			@Override
			public String doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setInt(1, userId);
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					return rs.getString("author");
				}
				return null;
			}
		});
	}
	
	public String queryForStartYear(final int postId){
		return jdbcTemplate.execute(sql_select_start_year, new PreparedStatementCallback<String>(){
			@Override
			public String doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setLong(1,postId);
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					return rs.getString("start_year");
				}
				return null;
			}
		});
	}
	
	
	public List<CmsGenre> queryForGenre(final int postId){
		return jdbcTemplate.execute(sql_select_genre, new PreparedStatementCallback<List<CmsGenre>>(){
			@Override
			public List<CmsGenre> doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				List<CmsGenre> genreList = new ArrayList<CmsGenre>();
				ps.setLong(1,postId);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					genreList.add( new CmsGenre(rs.getInt("genre_id"),rs.getString("genre_name")));
				}
				return genreList;
			}
		});
	}
	
	public Pair<String,String> queryForCity(final int postId) {
		return jdbcTemplate.execute(sql_select_city, new PreparedStatementCallback<Pair<String,String>>(){
			@Override
			public Pair<String,String> doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setLong(1,postId);
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					return Pair.of(rs.getString("city"),rs.getString("coordinate"));
				}
				return null;
			}
		});
	}
	
	public  List<String> queryForArtists(final int postId){
		return jdbcTemplate.execute(sql_select_artists, new PreparedStatementCallback<List<String>>(){
			@Override
			public List<String> doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setLong(1,postId);
				ResultSet rs = ps.executeQuery();
				List<String> artists = new ArrayList<String>();
				while(rs.next()){
					artists.add(rs.getString("artist"));
				}
				return artists;
			}
		});
	}
	
	public List<String> queryForOrganizations(final int postId){
		return jdbcTemplate.execute(sql_select_organizations, new PreparedStatementCallback<List<String>>(){
			@Override
			public List<String> doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setLong(1,postId);
				ResultSet rs = ps.executeQuery();
				List<String> organizations = new ArrayList<String>();
				while(rs.next()){
					organizations.add(rs.getString("organization"));
				}
				return organizations;
			}
		});
	}
	
	public void queryForSlide(final int postId,final List<CmsSlide> slides){
		 final StringBuilder slideClause = new StringBuilder(sql_select_slide).append(postId).append(" and meta_key in (");
		 Joiner.on(",").appendTo(slideClause, slides);
		 slideClause.append(")");
		 jdbcTemplate.execute(new StatementCallback<Void>(){

			@Override
			public Void doInStatement(Statement stmt) throws SQLException,
					DataAccessException {
				ResultSet rs= stmt.executeQuery(slideClause.toString());
				while(rs.next() ){
					String metaKey = rs.getString("meta_key");
					String metaValue = rs.getString("meta_value");
					Map<String,Object> values =JsonUtil.getJsonObj(metaValue);
					for(CmsSlide _slide:slides){
						if(metaKey.equals(_slide.getMyMediaId())){
							_slide.setImageMemo((String)values.get("zy_media_memo"));
							_slide.setImageTitle((String)values.get("zy_media_title"));
							break;
						}
					}
				
				}
				return null;
			}
			 
			 
		 });
		 
	}
	
	
	public Map<String,String> queryForDataMedia(final Collection<String> metaKeys){
		 final Iterator<String> metaKeyIt = metaKeys.iterator();
		 Iterator<String> _metaKeyWithSingleQuotaIterator = new Iterator<String>(){
			 
			 
			@Override
			public boolean hasNext() {
				return metaKeyIt.hasNext();
			}

			@Override
			public String next() {
				return "'"+metaKeyIt.next()+"'";
			}

			@Override
			public void remove() {
				metaKeyIt.remove();
				
			}
			 
		 };
		 
		
		 final StringBuilder slideClause = new StringBuilder(sql_select_data_media).append(" (");
		 Joiner.on(",").appendTo(slideClause, _metaKeyWithSingleQuotaIterator);
		 slideClause.append(")");
		 return jdbcTemplate.execute(new StatementCallback<Map<String,String>>(){

			@Override
			public Map<String,String> doInStatement(Statement stmt) throws SQLException,
					DataAccessException {
				Map<String,String>  map  = new LinkedHashMap<String,String>();
				ResultSet rs= stmt.executeQuery(slideClause.toString());
				while(rs.next()){
					String metaKey = rs.getString("meta_key");
					String metaValue = rs.getString("meta_value");
					map.put(metaKey,metaValue);
				}
				return map;
			}
			 
			 
		 });
		 
	}
	
	public Integer updateLockStatus(final int docId){
		return jdbcTemplate.execute(sql_update_lock_status, new PreparedStatementCallback<Integer>(){

			@Override
			public Integer doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setInt(1, docId);
				return ps.executeUpdate();
			}
			
		});
	}
	
	public Integer insertLog(final String type,final String level,final String message,final Date logTime){
		return jdbcTemplate.execute(sql_insert_log, new PreparedStatementCallback<Integer>(){

			@Override
			public Integer doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setString(1, type);
				ps.setString(2, level);
				ps.setString(3, message);
				ps.setTimestamp(4, new Timestamp(logTime.getTime()));
				return ps.executeUpdate();
			}
			
		});
	}

}
