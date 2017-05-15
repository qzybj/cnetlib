/*******************************************************************************
 * @project:
 * @author: Cuckoo
 * @created: 2013-5-23
 * @purpose:
 *		处理与List相关的通用方法，如过滤非空的情况
 * @version: 1.0
 *
 * Revision History at the end of file.
 ******************************************************************************/
package com.cnet.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CListUtil {
	
	/**
	 * 当目标List为空时，为起实例一个，防止空指针异常
	 * @param tagetList
	 * 		需要判断的List
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List filter(List tagetList){
		if( null == tagetList){
			tagetList = new ArrayList(0);
		}
		return tagetList ;
	}

	public static HashMap f(HashMap targetMap){
		if( null == targetMap){
			targetMap = new HashMap();
		}
		return targetMap;
	}

	public static LinkedHashMap f(LinkedHashMap targetMap){
		if( null == targetMap){
			targetMap = new LinkedHashMap();
		}
		return targetMap;
	}

    /**
     * 获取集合长度
     * @param tagetList
     * @return
     */
    public static int getSize(List tagetList){
        if( null == tagetList ){
           return 0;
        }
        return tagetList.size() ;
    }

	/**
	 * 获取集合长度
	 * @param params
	 * @return
     */
	public static int getSize(Object[] params){
		if(params == null){
			return 0 ;
		}
		return params.length ;
	}

	@SuppressWarnings("rawtypes")
	public static HashSet filterHashSet(HashSet target){
		if( null == target){
			target = new HashSet();
		}
		return target ;
	}
	
	/**
	 * 清除ArrayList，如果为null为其创建一对象，如果不为null，清空list中数据
	 * @param tagetList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List clear(List tagetList){
		if( tagetList == null ){
			tagetList = new ArrayList(0);
		}else {
			tagetList.clear();
		}
		return tagetList;
	}
	
	@SuppressWarnings("rawtypes")
	public static HashSet clear(HashSet target){
		if( target == null ){
			target = new HashSet(0);
		}else {
			target.clear();
		}
		return target;
	}

	/**
	 * 检查当前list是否为空
	 * @param tagetList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(List tagetList){
		if( tagetList == null ||
				tagetList.size() == 0 ){
			return true ;
		}
		return false ;
	}
	
	/**
     * 判断是否为空
     * @param objList
     * @return
     */
    public static boolean isEmpty(Object[] objList){
        if(objList == null ||
                objList.length == 0){
            return true ;
        }
        return false;
    }

	/**
	 * 判断map是否为空
	 * @param target
	 * @return
	 */
	public static boolean isEmpty(Map target){
		if(target == null ||
				target.size() == 0){
			return true ;
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isEmptySet(HashSet target){
		if( target == null ||
				target.size() == 0 ){
			return true ;
		}
		return false ;
	}
	
	/**
	 * release Map
	 * @param targetMap
	 */
	@SuppressWarnings("rawtypes")
	public static void clear(Map targetMap){
		if( targetMap != null ){
			targetMap.clear() ;
		}
	}
	
}
/*******************************************************************************
 * Revision History [type 'revision' & press Alt + '/' to insert revision block]
 * 
 * [Revision on 2013-5-23 14:08:09 by Cuckoo]<BR>
 * 	 Create method: filterArrayList()  
 ******************************************************************************/