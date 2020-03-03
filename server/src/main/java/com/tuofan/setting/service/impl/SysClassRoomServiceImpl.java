package com.tuofan.setting.service.impl;

import com.tuofan.setting.entity.SysClassRoom;
import com.tuofan.setting.mapper.SysClassRoomMapper;
import com.tuofan.setting.service.ISysClassRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-03-01
 */
@Service
public class SysClassRoomServiceImpl extends ServiceImpl<SysClassRoomMapper, SysClassRoom> implements ISysClassRoomService {

    @Override
    public List listV() {
        return baseMapper.listV();
    }
}
