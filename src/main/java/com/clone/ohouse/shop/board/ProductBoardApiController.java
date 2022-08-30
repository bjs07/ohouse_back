package com.clone.ohouse.shop.board;

import com.clone.ohouse.shop.board.domain.ProductBoardService;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardResponseDto;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardSaveRequestDto;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardUpdateRequestDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ProductBoardApiController {
    private final ProductBoardService boardService;

    @ApiOperation(
            value = "제품 게시글 등록",
            notes = "판매자가 제품을 등록하기 위해 사용합니다.",
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/store/productions")
    public void save(@RequestBody ProductBoardSaveRequestDto saveRequestDto) {
        boardService.save(saveRequestDto);
    }

    @ApiOperation(
            value = "제품 게시글 수정",
            notes = "판매자 혹은 관리자가 게시글 수정에 사용합니다.",
            code = 200)
    @ApiResponse(code = 500, message = "server error")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "제품 게시글의 id (제품 id X)"),
            @ApiImplicitParam(name = "updateRequestDto", value = " df dfsf")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/store/productions/{id}")
    public void update(@PathVariable Long id, @RequestBody ProductBoardUpdateRequestDto updateRequestDto) {
        boardService.update(id, updateRequestDto);
    }

    @ApiOperation(
            value = "제품 게시글 얻기",
            notes = "게시글에 대한 모든 내용을 가져옵니다. (title, content, 작성자, 수정일시, 수정자 등)",
            code = 200)
    @ApiResponse(code = 500, message = "server error")
    @ApiImplicitParam(name = "id", value = "제품 게시글의 id (제품 id X)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/store/productions/{id}")
    public ProductBoardResponseDto findById(@PathVariable Long id) {
        return boardService.findById(id);
    }
    @ApiOperation(
            value = "제품 게시글 삭제",
            notes = "게시글을 삭제합니다.",
            code = 204)
    @ApiResponse(code = 500, message = "server error")
    @ApiImplicitParam(name = "id", value = "제품 게시글의 id (제품 id X)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/store/productions/{id}")
    public void delete(@PathVariable Long id) {
        boardService.delete(id);
    }

}
