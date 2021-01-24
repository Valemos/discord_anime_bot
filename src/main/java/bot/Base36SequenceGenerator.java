package bot;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.Serializable;
import java.util.Properties;

@Converter
public class Base36SequenceGenerator extends SequenceStyleGenerator {


    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return StringUtils.leftPad(Long.toString((Long) super.generate(session, object), 36), 4, '0');
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        super.configure(LongType.INSTANCE, params, serviceRegistry);
    }
}
