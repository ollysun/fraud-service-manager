package com.etz.fraudeagleeyemanager.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import static com.etz.fraudeagleeyemanager.constant.AppConstant.PAGE;
import static com.etz.fraudeagleeyemanager.constant.AppConstant.PAGE_LIMIT;;

@Data
public class MetaData<T>{

    @JsonProperty("total_record")
    private Long total;

    @JsonProperty("limit")
    private int perPage;

    @JsonProperty("current_page")
    private int currentPage;

    @JsonProperty("total_page")
    private int totalPage;

    @JsonProperty("next_page")
    private Long nextPage;
    
    @JsonProperty("prev_page")
    private Long prevPage;
    
    @JsonProperty("next_page_url")
    private String nextPageUrl;

    @JsonProperty("prev_page_url")
    private String prevPageUrl;

    private Long from;
    private Long to;

    public MetaData(Page<T> result){

        setTotal(result.getTotalElements());

        setPerPage(Integer.parseInt(RequestUtil.perPage()));

        int nexttPage = RequestUtil.getPage() <= 0 ? 1 : RequestUtil.getPage() + 1;

        int prevvPage = RequestUtil.getPage() <= 0 ? 0 : RequestUtil.getPage() - 1;

        if(!result.getContent().isEmpty()){
            if(result.isFirst() && result.isLast()){
            	setPrevPage(null);
            	setNextPage(null);
                setNextPageUrl(null);
                setPrevPageUrl(null);
            }else if(!result.isFirst() && !result.isLast()){
            	setPrevPage(Long.valueOf(prevvPage));
            	setNextPage(Long.valueOf(nexttPage));
                setPrevPageUrl( RequestUtil.getRequest().getRequestURL().append("?").append(PAGE_LIMIT).append("=").append(getPerPage()).append("&").append(PAGE).append("=").append(prevPage).toString() );
                setNextPageUrl( RequestUtil.getRequest().getRequestURL().append("?").append(PAGE_LIMIT).append("=").append(getPerPage()).append("&").append(PAGE).append("=").append(nextPage).toString() );
            }else if(result.isFirst() && !result.isLast()){
            	setPrevPage(null);
            	setNextPage(Long.valueOf(nexttPage));
                setPrevPageUrl( null );
                setNextPageUrl( RequestUtil.getRequest().getRequestURL().append("?").append(PAGE_LIMIT).append("=").append(getPerPage()).append("&").append(PAGE).append("=").append(nextPage).toString() );
			} else if (!result.isFirst() && result.isLast()) {
				setPrevPage(Long.valueOf(prevvPage));
				setNextPage(null);
				setPrevPageUrl( RequestUtil.getRequest().getRequestURL().append("?").append(PAGE_LIMIT).append("=").append(getPerPage()).append("&").append(PAGE).append("=").append(prevPage).toString());
				setNextPageUrl(null);
            }

            setTotalPage(result.getTotalPages());
            setCurrentPage(RequestUtil.getPage());
            int $from = ((RequestUtil.getPage() - 1) * getPerPage()) + 1;
            int $to = ((RequestUtil.getPage() - 1) * getPerPage()) + result.getContent().size();
            setFrom(Long.valueOf($from + ""));
            setTo(Long.valueOf($to + ""));
        }
    }
}
