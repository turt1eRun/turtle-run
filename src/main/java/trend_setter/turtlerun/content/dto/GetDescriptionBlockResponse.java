package trend_setter.turtlerun.content.dto;

import trend_setter.turtlerun.content.constant.BlockType;
import trend_setter.turtlerun.content.entity.DescriptionBlock;

public record GetDescriptionBlockResponse(Long id, BlockType type, String text,
                                          GetFileResponse descriptionFileResponse, int orderNum) {

    public static GetDescriptionBlockResponse from(DescriptionBlock block) {
        return new GetDescriptionBlockResponse(
            block.getId(), block.getType(),
            block.getType() == BlockType.TEXT ?
                block.getText() : null,
            block.getType() == BlockType.IMAGE ?
                GetFileResponse.from(block.getDescriptionFile()) : null,
            block.getOrderNum());
    }
}
