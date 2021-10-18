package com.gw.tools;


import java.rmi.Remote;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.database.EnvironmentRepository;
import com.gw.database.HistoryRepository;
import com.gw.database.HostRepository;
import com.gw.jpa.Environment;
import com.gw.jpa.History;
import com.gw.jpa.Host;
import com.gw.local.LocalSession;
import com.gw.utils.BaseTool;
import com.gw.utils.RandomString;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HostTool {

	Logger logger = Logger.getLogger(HostTool.class);
	
	@Autowired
	HostRepository hostrepository;
	
	@Autowired
	ProcessTool pt;
	
	@Autowired
	HistoryRepository historyrepository;
	
	@Autowired
	BaseTool bt;

	@Autowired
	LocalhostTool lt;

	@Autowired
	RemotehostTool rt;
	
	@Autowired
	EnvironmentRepository environmentrepository;
	
	
	/**
	 * Judge if the host is localhost
	 * @param hid
	 * @return
	 */
	public boolean islocal(String hid) {
		
		boolean is = false;
		
		Optional<Host> opthost = hostrepository.findById(hid);

		if(opthost.isPresent()){
			Host h = hostrepository.findById(hid).get();
		
			if("127.0.0.1".equals(h.getIp()) || "localhost".equals(h.getIp())) {
				
				is = true;
				
			}
		}
		
		
		return is;
		
	}
	
	/**
	 * Get History by ID
	 * @param hid
	 * @return
	 */
	public String one_history(String hid) {
		
		StringBuffer resp = new StringBuffer();
		
		try {
			
			History his = historyrepository.findById(hid).get();
			
				resp.append("{ \"hid\": \"").append(his.getHistory_id()).append("\", ");
				
				resp.append("\"id\": \"").append(his.getHistory_id()).append("\", ");
				
				resp.append("\"process\": \"").append(his.getHistory_process()).append("\", ");
				
				resp.append("\"name\": \"").append(his.getHistory_input()).append("\", ");
				
				resp.append("\"begin_time\":\"").append(his.getHistory_begin_time()).append("\", ");
				
				resp.append("\"end_time\":\"").append(his.getHistory_end_time()).append("\", ");
				
				resp.append("\"input\":\"").append(his.getHistory_input()).append("\", ");
				
				resp.append("\"output\":\"").append(pt.escape(his.getHistory_output())).append("\", ");
				
				resp.append("\"host\":\"").append(his.getHost_id()).append("\", ");
				
				resp.append("\"status\":\"").append(his.getIndicator()).append("\" }");
				
			
		} catch (Exception e) {
		
			e.printStackTrace();
			
		}
		
		return resp.toString();
		
	}
	
	
	
	public String recent(String hostid, int limit) {
		
		StringBuffer resp = new StringBuffer();
		
		Collection<History> historylist = historyrepository.findRecentHistory(hostid, limit);
		
		try {
			
			resp.append("[");
			
			int num = 0;
			
			Iterator<History> hisint = historylist.iterator();
			
			while(hisint.hasNext()) {
				
				if(num!=0) {
					
					resp.append(", ");
					
				}
				
				History h = hisint.next();

				resp.append("{ \"id\": \"").append(h.getHistory_id()).append("\", ");
				
				resp.append("\"name\": \"").append(h.getHistory_process()).append("\", ");
				
				resp.append("\"end_time\": \"").append(h.getHistory_end_time()).append("\", ");

				resp.append("\"notes\": \"").append(h.getHistory_notes()).append("\", ");
				
				resp.append("\"status\": \"").append(h.getIndicator()).append("\", ");
				
				resp.append("\"begin_time\": \"").append(h.getHistory_begin_time()).append("\"}");
				
				num++;
				
			}
			
			resp.append("]");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return resp.toString();
		
	}
	
	/**
	 * Detail JSON object
	 * @param id
	 * @return
	 */
	public String detailJSONObj(String id) {
		
		Host h = hostrepository.findById(id).get();
		
		String json = toJSON(h);
		
		return json;
		
	}
	
	public String detail(String id) {
		
		String detail = detailJSONObj(id);
		
		return detail;
		
	}
	
	public Host getHostById(String id) {
		
		Optional<Host> oh = hostrepository.findById(id);


		Host h = oh.isPresent()?oh.get():null;
		
		return h;
	}
	
	public String[] getHostDetailsById(String id) {
		
		String[] hostdetails = new String[6] ;
		
		try {
			
			Host h = hostrepository.findById(id).get();
				
			hostdetails[0] = h.getName();
			
			hostdetails[1] = h.getIp();
			
			hostdetails[2] = h.getPort();
			
			hostdetails[3] = h.getUsername();
			
			hostdetails[4] = h.getType();
			
			hostdetails[5] = h.getUrl();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return hostdetails;
	}
	
	public String toJSON(Host h) {
		
		String json = "{}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(h);
            // logger.debug("ResultingJSONstring = " + json);
            //System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return json;
		
	}
	
	public String toJSON(Environment env) {
		
		String json = "{}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(env);
            // logger.debug("ResultingJSONstring = " + json);
            //System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return json;
		
	}
	
	// public String listWithEnvironments(String owner){

	// 	Iterator<Host> hostit = hostrepository.findAllPublicHosts().iterator();
		
	// 	List<Host> hostlist = new ArrayList();

	// 	hostit.forEachRemaining(hostlist::add);

	// 	hostit = hostrepository.findPrivateByOwner(owner).iterator();

	// 	hostit.forEachRemaining(hostlist::add);

		
		
		
	// }

	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public String list(String owner) {
		
		StringBuffer json = new StringBuffer("[");

		List<Host> hostlist = new ArrayList();
		
		Iterator<Host> hostit = hostrepository.findAllPublicHosts().iterator();

		hostit.forEachRemaining(hostlist::add);
		
		hostit = hostrepository.findPrivateByOwner(owner).iterator();

		hostit.forEachRemaining(hostlist::add);

		hostlist.forEach((host)->{json.append(toJSON(host)).append(",");});

		json.deleteCharAt(json.length() - 1);
		
		json.append("]");
		
		return json.toString();
		
	}
	
	
	/**
	 * Add a new host
	 * @param hostname
	 * @param hostip
	 * @param hostport
	 * @param username
	 * @param owner
	 */
	public String add(String hostname, String hostip, String hostport, String username, String url, String type, String owner, String confidential) {
		
		String newhostid = new RandomString(6).nextString();
		
		Host h = new Host();

		if(bt.isNull(owner)) owner = "111111"; //default to be the public user
		
		h.setId(newhostid);
		h.setIp(hostip);
		h.setName(hostname);
		h.setOwner(owner);
		h.setPort(hostport);
		h.setType(type);
		h.setUrl(url);
		h.setUsername(username);
		h.setConfidential(confidential);
		
		hostrepository.save(h);
		
		return newhostid;
		
	}
	
	/**
	 * Remove a host from database
	 * @param hostid
	 */
	public String del(String hostid) {
		
		hostrepository.deleteById(hostid);
		
		return "done";
		
	}

	public void save(Host h){

		hostrepository.save(h);

	}

	public List<Host> getAllHosts(){

		List<Host> hostlist = new ArrayList();

		hostrepository.findAll().forEach(h->hostlist.add(h));

		return hostlist;

	}

	public void saveEnvironment(Environment newenv){
		environmentrepository.save(newenv);
	}

	/**
	 * Add environment to database
	 * @param historyid
	 * @param bin
	 * @param env
	 * @param basedir
	 * @return
	 */
	public String addEnv(String historyid, String hostid, String type, String bin, String env, String basedir, String settings) {
		
		String resp = null;
		
		try {
			
			if(!bt.isNull(bin) && !bt.isNull(env) && !bt.isNull(basedir)){
				
				Iterator<Environment> eit = environmentrepository.findEnvByID_BIN(hostid, bin).iterator();
				
				Environment newenv = new Environment();
				
				if(eit.hasNext()) {
					
					newenv = eit.next();
					
				}else {
					
					newenv.setId(historyid);
					
					newenv.setName(bin+"-"+env+"-"+basedir);
					
					newenv.setType(type);
					
					newenv.setBin(bin);
					
					newenv.setPyenv(env);
					
					newenv.setBasedir(basedir);
					
					newenv.setSettings(settings);
					
					// newenv.setHost(hostid);
					newenv.setHostobj(this.getHostById(hostid));
					
					environmentrepository.save(newenv);
					
				}

			}else{

				logger.debug("one of the bin, env, basedir, settings is null and the environment will not be saved into database.");

			}
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		return resp;
		
	}
	
	public void showAllEnvironment() {
		

		String resp = null;
		
		try {
			
			Iterator<Environment> envit = environmentrepository.findAll().iterator();
			
			StringBuffer envstr = new StringBuffer();
			
			envstr.append("[");
			
			int num = 0;
			
			while(envit.hasNext()) {
				
				if(num!=0) {
					
					envstr.append(", ");
					
				}
				
				Environment newenv = envit.next();
				
				envstr.append(toJSON(newenv));
				
			}
			
			envstr.append("]");
			
			resp = envstr.toString();
			
			logger.debug(resp);
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
	}
	
	public String getEnvironmentByBEB(String hostid, String bin, String env, String basedir) {
		
		String resp = null;
		
		try {
			
			Collection<Environment> envlist = environmentrepository.findEnvByID_BIN_ENV_BaseDir(hostid, bin, env, basedir);
			
			StringBuffer envstr = new StringBuffer();
			
			envstr.append("[");
			
			int num = 0;
			
			Iterator<Environment> it = envlist.iterator();
			
			while(it.hasNext()) {
				
				if(num!=0) {
					
					envstr.append(", ");
					
				}
				
				Environment newenv = it.next();
				
				envstr.append(toJSON(newenv));
				
			}
			
			envstr.append("]");
			
			resp = envstr.toString();
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		return resp;
		
	}
	/**
	 * Find all the available python environments on this machine
	 * @param hid
	 * @param password
	 * @return
	 */
	public String readEnvironment(String hid, String password){

		String resp = null;

		if(this.islocal(hid)){

			resp = lt.readPythonEnvironment(hid, password);

		}else{

			resp = rt.readPythonEnvironment(hid, password);
			
		}

		return resp;

	}

	public Environment getEnvironmentByBin(String bin, List<Environment> envlist){
		
		Environment theenv = null;

		if(bin.length()>255){

			logger.info("The BIN is too long to save. Pass.");
			
		}else{
			
			for(Environment env: envlist){

				if(!bt.isNull(env.getBin()) && env.getBin().equals(bin)){
	
					theenv = env;
	
					break;
				}
	
			}
		
		}


		return theenv;
	}

	public boolean checkIfEnvironmentExist(String bin, List<Environment> envlist){

		boolean exists = false;

		for(Environment env: envlist){

			if(!bt.isNull(env.getBin()) && env.getBin().equals(bin)){

				exists = true;

				break;
			}

		}

		return exists;

	}

	/**
	 * 
	 * @param hid
	 * @return
	 */
	public List<Environment> getEnvironmentsByHostId(String hid){

		List<Environment> envlist = new ArrayList();
		
		try {
			
			Collection<Environment> envquerylist = environmentrepository.findEnvByHost(hid);
			
			Iterator<Environment> it = envquerylist.iterator();
			
			while(it.hasNext()) {
				
				Environment newenv = it.next();

				envlist.add(newenv);
			
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		return envlist;
	}
	
	/**
	 * Get environments by host
	 * @param hid
	 * @return
	 */
	public String getEnvironments(String hid) {
		
		String resp = null;
		
		try {
			
			List<Environment> envlist = getEnvironmentsByHostId(hid);
			
			StringBuffer envstr = new StringBuffer("[");

			int num = 0;

			for(Environment env: envlist) {
				
				if(num!=0) {
					
					envstr.append(", ");
					
				}
				
				envstr.append(toJSON(env));
				
				num++;
				
			}
			
			envstr.append("]");
			
			resp = envstr.toString();
			
			// logger.debug("the python environment for host: " + hid + " " + resp);
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		return resp;
	}

	public void addNewEnvironment(String pypath, List<Environment> old_envlist, String hostid, String name){

		Environment theenv = this.getEnvironmentByBin(pypath, old_envlist);

		if(bt.isNull(theenv)){

			Environment env = new Environment();
			env.setId(new RandomString(6).nextString());
			env.setBin(pypath);
			env.setName(name);
			// env.setHost(hostid);
			env.setHostobj(this.getHostById(hostid));
			// env.setBasedir(line); //the execution place which is unknown at this point
			if(pypath.contains("conda"))
				env.setPyenv("anaconda");
			else
				env.setPyenv("pip");
			env.setSettings(""); //set the list of dependencies like requirements.json or .yaml
			env.setType("python"); //could be python or shell. R is not supported yet. 
			env.setBasedir("~");
			this.saveEnvironment(env);

		}

	}


	/**
	 * Update the Host table
	 * @param hostname
	 * @param hostip
	 * @param hostport
	 * @param username
	 * @param type
	 * @param object
	 * @return
	 */
	public String update(String hostid, String hostname, String hostip, String hostport, String username, String type, String owner, String url, String confidential) {

		String resp = null;
		
		try {
			
			Host h = this.getHostById(hostid);
			
			// h.setId(hostid);
			
			h.setName(hostname);
			
			if(!bt.isNull(hostip)) h.setIp(hostip);
			
			if(!bt.isNull(hostport)) h.setPort(hostport);
			
			if(!bt.isNull(username)) h.setUsername(username);
			
			if(!bt.isNull(type)) h.setType(type);
			
			
			if(!bt.isNull(owner)) h.setOwner(owner);
			
			if(!bt.isNull(url)) h.setUrl(url);

			if(!bt.isNull(confidential)) h.setConfidential(confidential);
			
			hostrepository.save(h);
			
		} catch (Exception e) {

			e.printStackTrace();
			
			logger.error("Failed to update the host table " + e.getLocalizedMessage());
			
		}
		
		return resp;
		
	}
	
	
	

}
