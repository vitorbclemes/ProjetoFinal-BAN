package persistencia;

//SQL Imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//JAVA.UTIL imports
import java.util.List;
import java.util.ArrayList;

//DATA Imports
import dados.Edicao;

//EXCEPTIONS Imports
import exceptions.*;

public class EdicaoDAO {
    public static EdicaoDAO instance = null;

    private PreparedStatement insert;
    private PreparedStatement delete;
	private PreparedStatement update;
	private PreparedStatement select;
	private PreparedStatement selectAll;
	private PreparedStatement newId;
    private PreparedStatement selectAllWhereUFisRS;

    private EdicaoDAO() throws SQLException,ClassNotFoundException{
        Connection conexao = Conexao.getConexao();

        insert = conexao.prepareStatement("insert into edicoes values (?,?,?,?,?)");
		delete = conexao.prepareStatement("delete from edicoes where edicaoid = ?");
		update = conexao.prepareStatement("update edicoes set cidade = ?,uf = ?,qtdparticipantes = ?, ano = ? where edicaoid = ?");
		select = conexao.prepareStatement("select *	from edicoes where edicaoid = ?");
		selectAll = conexao.prepareStatement("select * from edicoes");
		newId = conexao.prepareStatement("select nextval('edicoes_id_seq')"); 

        //Permitir uma consulta com subconsulta (OK)
        //Retorna as edicoes que ocorreram no estado de RS
        selectAllWhereUFisRS = conexao.prepareStatement("select  * from edicoes where cidade in (select e1.cidade from edicoes e1 where e1.uf = 'RS')");
    }

    
    /** 
     * Inicia a instancia DAO caso ela nao exista
     * @return EdicaoDAO
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static EdicaoDAO getInstance() throws SQLException,ClassNotFoundException{
        if(instance == null)
            instance = new EdicaoDAO();
        return instance;
    }

    
    /** 
     * Deleta uma edicao do DB
     * @param ed
     * @throws DeleteException
     */
    public void delete(Edicao ed) throws DeleteException{
        try{
            delete.setInt(1,ed.getEdicaoid());
            delete.executeUpdate();
        } catch(Exception e){
            throw new DeleteException("Erro na delecao");
        }
    }

    
    /** 
     * Gera um novo ID disponivel
     * @return int
     * @throws InsertException
     */
    public int newId() throws InsertException{
        try{
            ResultSet rs = newId.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
            else{
                return 0;
            }
        }catch(SQLException e){
            throw new InsertException("Nao foi possivel inserir a edicao");
        }
    }

    
    /** 
     * Insere uma edicao no DB
     * @param ed
     * @throws InsertException
     */
    public void insert(Edicao ed) throws InsertException{
        try{
            ed.setEdicaoid(newId());
            insert.setInt(1, ed.getEdicaoid());
            insert.setString(2, ed.getCidade());
            insert.setString(3, ed.getUf());
            insert.setInt(4, ed.getQtdparticipantes());
            insert.setInt(5, ed.getAno());
            insert.executeUpdate();
        }catch(Exception e){
            throw new InsertException("Nao foi possivel inserir a edicao");
        }
    }

    
    /** 
     * Seleciona um edicao do DB
     * @param edicaoid
     * @return Edicao
     * @throws SelectException
     */
    public Edicao select(int edicaoid) throws SelectException{
        Edicao edicao = null;
        try{
            select.setInt(1, edicaoid);
            ResultSet rs = select.executeQuery();
            if(rs.next()){
                edicao = new Edicao();
                edicao.setEdicaoid(rs.getInt("edicaoid"));
                edicao.setCidade(rs.getString("cidade"));
                edicao.setUf(rs.getString("uf"));
                edicao.setQtdparticipantes(rs.getInt("qtdparticipantes"));
                edicao.setAno(rs.getInt("ano"));
            }
            return edicao;
        }catch(Exception e){
            throw new SelectException("Nao foi possivel selecionar a edicao");
        }
    }

    
    /** 
     * Seleciona todas as edicoes do DB
     * @return List<Edicao>
     * @throws SelectException
     */
    public List<Edicao> selectAll() throws SelectException{
        List<Edicao> edicoes = new ArrayList<Edicao>();
        Edicao edicao = null;
        try{
            ResultSet rs = selectAll.executeQuery();
            while ( rs.next() ){
                edicao = new Edicao();
                edicao.setEdicaoid(rs.getInt("edicaoid"));
                edicao.setCidade(rs.getString("cidade"));
                edicao.setUf(rs.getString("uf"));
                edicao.setQtdparticipantes(rs.getInt("qtdparticipantes"));
                edicao.setAno(rs.getInt("ano"));

                edicoes.add(edicao);
            }
            return edicoes;
        }catch (Exception e){
            throw new SelectException("Nao foi possivel selecionar a edicao");
        }
    }

    
    /** 
     * Atualiza uma edicao do DB
     * @param ed
     * @throws UpdateException
     */
    public void update (Edicao ed) throws UpdateException{
        try{
            update.setString(1, ed.getCidade());
            update.setString(2, ed.getUf());
            update.setInt(3, ed.getQtdparticipantes());
            update.setInt(4, ed.getAno());
            update.setInt(5, ed.getEdicaoid());
            update.executeUpdate();
        }catch (SQLException e){
            throw new UpdateException("Nao foi possivel atualizar a edicao");
        }
    }

    
    /** 
     * Seleciona todas as edicoes que ocorreram no estado do Rio Grande do Sul
     * @return List<Edicao>
     * @throws SelectException
     */
    public List<Edicao> selectAllWhereUFisRS() throws SelectException{
        List<Edicao> edicoes = new ArrayList<Edicao>();
        Edicao edicao = null;
        try{
            ResultSet rs = selectAllWhereUFisRS.executeQuery();
            while ( rs.next() ){
                edicao = new Edicao();
                edicao.setEdicaoid(rs.getInt("edicaoid"));
                edicao.setCidade(rs.getString("cidade"));
                edicao.setUf(rs.getString("uf"));
                edicao.setQtdparticipantes(rs.getInt("qtdparticipantes"));
                edicao.setAno(rs.getInt("ano"));

                edicoes.add(edicao);
            }
            return edicoes;
        }catch (Exception e){
            throw new SelectException("Nao foi possivel selecionar a edicao");
        }
    }


}
