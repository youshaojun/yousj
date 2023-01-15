package top.yousj.uaa.service.impl;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.HikariDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yousj.core.constant.StrPool;
import top.yousj.core.exception.BizException;
import top.yousj.security.utils.AppNameHolder;
import top.yousj.uaa.entity.UaaUserDataSource;
import top.yousj.uaa.service.IUaaUserDataSourceService;

import javax.sql.DataSource;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final IUaaUserDataSourceService uaaUserDataSourceService;
	private final DynamicRoutingDataSource dynamicRoutingDataSource;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UaaUserDataSource uaaUserDataSource = uaaUserDataSourceService.getOne(Wrappers.<UaaUserDataSource>lambdaQuery()
			.eq(UaaUserDataSource::getAppName, AppNameHolder.get())
		);

		List<Object> authUrls = getUserInfo(uaaUserDataSource, username);

		return new UserDetails() {
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return CollectionUtils.isEmpty(authUrls) ? Collections.emptyList() :
					AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(StrPool.COMMA, authUrls.stream().map(String::valueOf).collect(Collectors.toSet())));
			}

			@Override
			public String getPassword() {
				return null;
			}

			@Override
			public String getUsername() {
				return username;
			}

			@Override
			public boolean isAccountNonExpired() {
				return true;
			}

			@Override
			public boolean isAccountNonLocked() {
				return true;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}

			@Override
			public boolean isEnabled() {
				return true;
			}
		};
	}

	private List<Object> getUserInfo(UaaUserDataSource uaaUserDataSource, String username) {

		if (Objects.isNull(uaaUserDataSource)) {
			throw new BizException("user datasource can not be null.");
		}

		String ds = uaaUserDataSource.getDs();

		if (!dynamicRoutingDataSource.getDataSources().containsKey(ds)) {
			addDataSource(ds, () -> {
				DataSourceProperty dataSourceProperty = new DataSourceProperty();
				BeanUtils.copyProperties(uaaUserDataSource, dataSourceProperty);
				return new HikariDataSourceCreator().createDataSource(dataSourceProperty);
			});
		}

		try {
			DynamicDataSourceContextHolder.push(ds);
			return SqlRunner.db().selectObjs(uaaUserDataSource.getQueryUserAuthSql(), username);
		} finally {
			DynamicDataSourceContextHolder.clear();
		}

	}

	private synchronized void addDataSource(String ds, Supplier<DataSource> dataSourceSupplier) {
		dynamicRoutingDataSource.addDataSource(ds, dataSourceSupplier.get());
	}

}
