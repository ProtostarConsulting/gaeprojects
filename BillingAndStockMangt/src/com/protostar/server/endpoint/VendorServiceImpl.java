package com.protostar.server.endpoint;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;

@Api(name = "vendorservice1", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.crm.server.endpoint", ownerName = "com.protostar.crm.server.endpoint", packagePath = ""))
public class VendorServiceImpl {

	/*@ApiMethod(name = "saveVendor")
	public MyBean saveVendor(VendorInfo vendorInfo) {
		MyBean myBean = new MyBean();
		EntityManager em = null;

		Vendor vendorEntity = VendorInfoUtil.toVendor(vendorInfo);

		if(vendorInfo.getId()==null)
		{
			myBean.setToken("R");
		}
		else
		{
			myBean.setToken("U");
		}
		try {
			em = EMF.get().createEntityManager();
			em.persist(vendorEntity);
			} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally
		{
		em.close();	
		}
		
		myBean.setMyData("Vendor Saved Successfully" +vendorEntity.getId()+vendorEntity.getEmail());
		return myBean;
	}

	@ApiMethod(name="getAllVendors")
	public List<VendorInfo> getAllVendors()
	{
		List<VendorInfo> resultList=new ArrayList<VendorInfo>();
		
		EntityManager em=null;
		
		try {
			em=EMF.get().createEntityManager();
			Query query= em.createQuery("select v from Vendor v");
			List<Vendor> resultlist2 = query.getResultList();
			
			for(Vendor v : resultlist2)
				resultList.add(VendorInfoUtil.toVendorInfo(v));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;	
	}*/
}
