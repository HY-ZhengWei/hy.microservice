package org.hy.microservice.common.probe.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.hy.common.ClassInfo;
import org.hy.common.ClassReflect;
import org.hy.common.Help;
import org.hy.common.xml.SerializableDef;
import org.hy.common.xml.log.Logger;

import org.hy.microservice.common.probe.enums.MetaFormat;
import org.hy.microservice.common.probe.schema.ProbeSchema;
import org.hy.microservice.common.probe.schema.ProbeSchemaDomain;
import org.hy.microservice.common.probe.schema.ProbeSchemaMeta;
import org.hy.microservice.common.probe.schema.ProbeSchemaMetaDomain;





/**
 * 用本体的定义构建数据探针的结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-10
 * @version     v1.0
 */
public class XOntology
{
    
    private static final Logger $Logger = new Logger(XOntology.class);
    
    
    
    /**
     * 解释本体定义的对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-11
     * @version     v1.0
     *
     * @param i_Data  本体数据
     * @return
     */
    public static <D extends SerializableDef> ProbeSchemaDomain parser(D i_Data)
    {
        ProbeSchemaDomain v_Schema = parser(i_Data.getClass());
        if ( v_Schema == null )
        {
            return null;
        }
        
        if ( v_Schema.getType().length() > 128 )
        {
            $Logger.error("Ontology[" + i_Data.getClass().getName() + "] type[" + v_Schema.getType() + "].lenght > 128");
            return null;
        }
        
        for (ProbeSchemaMetaDomain v_Meta : v_Schema.getName().getMetas())
        {
            v_Meta.setPropIndex(findPropertyIndex(i_Data ,v_Meta.getMname()));
            if ( v_Meta.getPropIndex() == null )
            {
                $Logger.error("Ontology[" + i_Data.getClass().getName() + "] mname[" + v_Meta.getMname() + "] not find PropIndex");
                return null;
            }
        }
        
        return v_Schema;
    }
    
    
    
    /**
     * 通过名称找属性下标值
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-11
     * @version     v1.0
     *
     * @param <D>
     * @param i_Data  
     * @param i_Name
     * @return
     */
    private static <D extends SerializableDef> Integer findPropertyIndex(D i_Data ,String i_Name)
    {
        for (int x=0; x<i_Data.gatPropertySize(); x++)
        {
            if ( i_Name.equals(i_Data.gatPropertyShortName(x)) )
            {
                return x;
            }
        }
        
        return null;
    }
    
    
    
    /**
     * 解释本体定义的对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-11
     * @version     v1.0
     *
     * @param i_Class  本体定义的对象类型
     * @return
     */
    private static ProbeSchemaDomain parser(Class<? extends SerializableDef> i_Class)
    {
        ClassInfo v_Ontology = ClassReflect.getAnnotation(i_Class ,Ontology.class);
        if ( v_Ontology == null )
        {
            return null;
        }
        
        ProbeSchema v_Schema       = new ProbeSchema();
        Ontology    v_OntologyType = v_Ontology.getAnnotation(Ontology.class);
        
        v_Schema.setType(Help.NVL(v_OntologyType.type() ,i_Class.getSimpleName()));
        v_Schema.setComment(Help.NVL(v_OntologyType.c() ,v_OntologyType.comment()));
        v_Schema.getName().setMetas(new ArrayList<ProbeSchemaMeta>());
        
        if ( !Help.isNull(v_Ontology.getFields()) )
        {
            for (Field v_Field : v_Ontology.getFields())
            {
                ProbeSchemaMeta v_Meta          = new ProbeSchemaMeta();
                Ontology        v_OntologyField = v_Field.getAnnotation(Ontology.class);
                Class<?>        v_Class         = v_Field.getType();
                
                v_Meta.setMname(v_Field.getName());
                v_Meta.setPk(v_OntologyField.pk() ? 1 : 0);
                v_Meta.setNullable(!v_OntologyField.n() ? 0 : (!v_OntologyField.nullable() ? 0 : 1));
                v_Meta.setComment(Help.NVL(v_OntologyField.c() ,v_OntologyField.comment()));
                
                if ( v_Field.getClass().equals(String.class) )
                {
                    v_Meta.setMformat(MetaFormat.String.getValue());
                }
                else if ( v_Class.equals(Integer.class)
                       || v_Class.equals(Long.class)
                       || v_Class.equals(Short.class) )
                {
                    v_Meta.setMformat(MetaFormat.Number.getValue());
                    
                    if ( v_OntologyField.d() > 0 )
                    {
                        v_Meta.setMdecimal(v_OntologyField.d());
                    }
                    else if ( v_OntologyField.decimal() > 0 )
                    {
                        v_Meta.setMdecimal(v_OntologyField.decimal());
                    }
                }
                else if ( v_Class.equals(Float.class)
                       || v_Class.equals(Double.class) )
                {
                    v_Meta.setMformat(MetaFormat.Number.getValue());
                    
                    if ( v_OntologyField.d() > 0 )
                    {
                        v_Meta.setMdecimal(v_OntologyField.d());
                    }
                    else if ( v_OntologyField.decimal() > 0 )
                    {
                        v_Meta.setMdecimal(v_OntologyField.decimal());
                    }
                }
                else if ( v_Class.equals(Boolean.class) )
                {
                    v_Meta.setMformat(MetaFormat.Boolean.getValue());
                }
                else
                {
                    v_Meta.setMformat(MetaFormat.String.getValue());
                }
                
                if ( v_Meta.getMformat().equals(MetaFormat.String.getValue())
                  || v_Meta.getMformat().equals(MetaFormat.Number.getValue()) )
                {
                    if ( v_OntologyField.l() > 0 )
                    {
                        v_Meta.setMlength(v_OntologyField.l());
                    }
                    else if ( v_OntologyField.length() > 0 )
                    {
                        v_Meta.setMlength(v_OntologyField.length());
                    }
                    else
                    {
                        $Logger.error("Ontology[" + i_Class.getName() + "] mname[" + v_Meta.getMname() + "] length invalid");
                        return null;
                    }
                }
                if ( v_Meta.getMformat().equals(MetaFormat.Number.getValue()) )
                {
                    if ( v_Meta.getMlength() <= v_Meta.getMdecimal() )
                    {
                        $Logger.error("Ontology[" + i_Class.getName() + "] mname[" + v_Meta.getMname() + "] length <= decimal");
                        return null;
                    }
                }
                
                v_Schema.getName().getMetas().add(v_Meta);
            }
        }
        
        if ( !Help.isNull(v_Ontology.getMethods()) )
        {
            for (Method v_Method : v_Ontology.getMethods())
            {
                ProbeSchemaMeta v_Meta           = new ProbeSchemaMeta();
                Ontology        v_OntologyMethod = v_Method.getAnnotation(Ontology.class);
                Class<?>        v_Class          = v_Method.getReturnType();
                
                if ( v_Method.getName().startsWith("get") )
                {
                    v_Meta.setMname(v_Method.getName().substring(3));
                }
                else if ( v_Method.getName().startsWith("is") )
                {
                    v_Meta.setMname(v_Method.getName().substring(2));
                }
                
                v_Meta.setPk(v_OntologyMethod.pk() ? 1 : 0);
                v_Meta.setNullable(!v_OntologyMethod.n() ? 0 : (!v_OntologyMethod.nullable() ? 0 : 1));
                v_Meta.setComment(Help.NVL(v_OntologyMethod.c() ,v_OntologyMethod.comment()));
                
                if ( v_Class.equals(Void.class) )
                {
                    $Logger.error("Ontology[" + i_Class.getName() + "] mname[" + v_Meta.getMname() + "] Return type is void");
                    return null;
                }
                else if ( v_Class.equals(String.class) )
                {
                    v_Meta.setMformat(MetaFormat.String.getValue());
                }
                else if ( v_Class.equals(Integer.class)
                       || v_Class.equals(Long.class)
                       || v_Class.equals(Short.class) )
                {
                    v_Meta.setMformat(MetaFormat.Number.getValue());
                    
                    if ( v_OntologyMethod.d() > 0 )
                    {
                        v_Meta.setMdecimal(v_OntologyMethod.d());
                    }
                    else if ( v_OntologyMethod.decimal() > 0 )
                    {
                        v_Meta.setMdecimal(v_OntologyMethod.decimal());
                    }
                }
                else if ( v_Class.equals(Float.class)
                       || v_Class.equals(Double.class) )
                {
                    v_Meta.setMformat(MetaFormat.Number.getValue());
                    
                    if ( v_OntologyMethod.d() > 0 )
                    {
                        v_Meta.setMdecimal(v_OntologyMethod.d());
                    }
                    else if ( v_OntologyMethod.decimal() > 0 )
                    {
                        v_Meta.setMdecimal(v_OntologyMethod.decimal());
                    }
                }
                else if ( v_Class.equals(Boolean.class) )
                {
                    v_Meta.setMformat(MetaFormat.Boolean.getValue());
                }
                else
                {
                    v_Meta.setMformat(MetaFormat.String.getValue());
                }
                
                if ( v_Meta.getMformat().equals(MetaFormat.String.getValue())
                  || v_Meta.getMformat().equals(MetaFormat.Number.getValue()) )
                {
                    if ( v_OntologyMethod.l() > 0 )
                    {
                        v_Meta.setMlength(v_OntologyMethod.l());
                    }
                    else if ( v_OntologyMethod.length() > 0 )
                    {
                        v_Meta.setMlength(v_OntologyMethod.length());
                    }
                    else
                    {
                        $Logger.error("Ontology[" + i_Class.getName() + "] mname[" + v_Meta.getMname() + "] length invalid");
                        return null;
                    }
                }
                if ( v_Meta.getMformat().equals(MetaFormat.Number.getValue()) )
                {
                    if ( v_Meta.getMlength() <= v_Meta.getMdecimal() )
                    {
                        $Logger.error("Ontology[" + i_Class.getName() + "] mname[" + v_Meta.getMname() + "] length <= decimal");
                        return null;
                    }
                }
                
                v_Schema.getName().getMetas().add(v_Meta);
            }
        }
        
        if ( Help.isNull(v_Schema.getName().getMetas()) )
        {
            $Logger.error("Ontology[" + i_Class.getName() + "] not find any field");
            return null;
        }
        
        int v_OrderBy = 0;
        for (ProbeSchemaMeta v_Item : v_Schema.getMetas())
        {
            v_Item.setOrderBy(++v_OrderBy);
        };
        
        return new ProbeSchemaDomain(v_Schema);
    }
    
    
    
    private XOntology()
    {
        
    }
    
}
